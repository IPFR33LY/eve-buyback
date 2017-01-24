package de.bahr.eve.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.tlabs.eve.api.Contract;
import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.entities.ExtendedContract;
import de.bahr.eve.entities.QuantifiedItem;
import de.bahr.eve.repositories.ExtendedContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by michaelbahr on 24/01/2017.
 */
@Service
public class ContractProcessor {

    @Autowired
    ContractItemsLoader contractItemsLoader;

    @Autowired
    AppraisalEstimator appraisalEstimator;

    @Autowired
    AppraisalPricer appraisalPricer;

    @Autowired
    ClientNameRetriever clientNameRetriever;

    @Autowired
    ExtendedContractRepository repo;

//    @Async
    public void parseContract(Contract contract, ApiEntry apiEntry) {
        System.out.println("Loading items for " + contract.getContractID());
        // todo: test this null check
        List<QuantifiedItem> quantifiedItems;
        try {
            quantifiedItems = contractItemsLoader.loadItems(contract, apiEntry);
        } catch (UnirestException e) {
            e.printStackTrace();
            return;
        }
        String link = appraisalEstimator.createLink(quantifiedItems);
        // todo: test this null check
        if (link == null) {
            System.out.println("Link for " +  contract.getContractID() + " was null. Aborting.");
            return;
        }
        Map<String, Double> priceData = appraisalPricer.priceContract(link);
        String clientName = clientNameRetriever.loadClientName(contract.getIssuerID());
        System.out.println("Finished api calls for " + contract.getContractID());

        ExtendedContract extendedContract = new ExtendedContract(contract, apiEntry);
        extendedContract.setQuantifiedItems(quantifiedItems);
        extendedContract.setLink(link);
        extendedContract.setJitaSell(priceData.get("jitaSell"));
        extendedContract.setJitaBuy(priceData.get("jitaBuy"));
        extendedContract.setBuybackBuy(priceData.get("buybackBuy"));
        extendedContract.setClient(clientName);
        extendedContract.setCorpId(apiEntry.getCorpId());
        extendedContract.setContractId(contract.getContractID());

        repo.deleteByContractId(contract.getContractID());
        repo.insert(extendedContract);
    }
}
