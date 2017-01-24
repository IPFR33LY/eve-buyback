package de.bahr.eve.services;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tlabs.eve.EveNetwork;
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.ContractItem;
import com.tlabs.eve.api.corporation.CorporationContractItemsRequest;
import com.tlabs.eve.net.DefaultEveNetwork;
import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.entities.QuantifiedItem;
import de.bahr.eve.util.TypeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelbahr on 24/01/2017.
 */
@Service
public class ContractItemsLoader {

    private EveNetwork eveNetwork = new DefaultEveNetwork();

    @Autowired
    TypeCache typeCache;

    public List<QuantifiedItem> loadItems(Contract contract, ApiEntry apiEntry) throws UnirestException {
        List<ContractItem> apiItems = getApiItems(apiEntry, contract.getContractID());
        List<QuantifiedItem> result = new ArrayList<>();
        for (ContractItem item : apiItems) {
            result.add(new QuantifiedItem(getTypeName(item.getTypeID()), item.getTypeID(), item.getQuantity()));
        }
        return result;
    }

    List<ContractItem> getApiItems(ApiEntry apiEntry, long contractID) {
        CorporationContractItemsRequest request = buildRequest(apiEntry, contractID);
        System.out.println("Getting items of " + contractID);
        return eveNetwork.execute(request).getItems();
    }

    CorporationContractItemsRequest buildRequest(ApiEntry apiEntry, long contractId) {
        CorporationContractItemsRequest request = new CorporationContractItemsRequest(apiEntry.getCorpId(), contractId);
        request.putParam("keyID", apiEntry.getKeyId());
        request.putParam("vCode", apiEntry.getvCode());
        return request;
    }

    String getTypeName(long typeId) throws UnirestException {
        String typeName = typeCache.get(typeId);
        if (null == typeName) {
            typeName = getTypeNameFromUrl(typeId);
            typeCache.set(typeName, typeId);
        }
        return typeName;
    }

    String getTypeNameFromUrl(long typeId) throws UnirestException {
        String url = "https://crest-tq.eveonline.com/inventory/types/" + typeId + "/";
        return Unirest.get(url).asJson().getBody().getObject().getString("name");
    }
}
