package de.bahr.eve.services;

import com.tlabs.eve.api.Contract;
import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.entities.ExtendedContract;
import de.bahr.eve.repositories.ExtendedContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by michaelbahr on 24/01/2017.
 */
@Service
public class ApiProcessor {

    @Autowired
    ContractsLoader contractsLoader;

    @Autowired
    ContractProcessor contractProcessor;

    @Autowired
    ExtendedContractRepository repo;

    @Async
    public void process(ApiEntry apiEntry) {
        List<Contract> contracts = contractsLoader.loadContracts(apiEntry);
        List<ExtendedContract> existingContracts = repo.findByCorpId(apiEntry.getCorpId());
        for (Contract contract : contracts) {
            if (isExchange(contract) && !isCompletedAndProcessedBefore(contract, existingContracts)) {
                contractProcessor.parseContract(contract, apiEntry);
            } else {
                System.out.println("Skipping " + contract.getContractID());
            }
        }
    }

    private boolean isCompletedAndProcessedBefore(Contract contract, List<ExtendedContract> existingContracts) {
        return isCompleted(contract) && isInList(contract.getContractID(), existingContracts);
    }

    private boolean isInList(long contractID, List<ExtendedContract> existingContracts) {
        for (ExtendedContract contract : existingContracts) {
            if (isSameContractId(contractID, contract)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSameContractId(long contractID, ExtendedContract contract) {
        return contractID == contract.getContract().getContractID();
    }

    private boolean isCompleted(Contract contract) {
        return Contract.Status.COMPLETED_COMPLETED.equals(contract.getStatus());
    }

    boolean isExchange(Contract contract) {
        return Contract.Type.EXCHANGE.equals(contract.getType());
    }
}
