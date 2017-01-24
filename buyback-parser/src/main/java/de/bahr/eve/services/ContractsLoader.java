package de.bahr.eve.services;

import com.tlabs.eve.EveNetwork;
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.ContractListResponse;
import com.tlabs.eve.api.corporation.CorporationContractsRequest;
import com.tlabs.eve.net.DefaultEveNetwork;
import de.bahr.eve.entities.ApiEntry;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by michaelbahr on 24/01/2017.
 */
@Service
public class ContractsLoader {

    private EveNetwork eveNetwork = new DefaultEveNetwork();

    public List<Contract> loadContracts(ApiEntry apiEntry) {
        CorporationContractsRequest request = buildRequest(apiEntry);
        ContractListResponse response = eveNetwork.execute(request);
        return response.getContracts();
    }

    CorporationContractsRequest buildRequest(ApiEntry apiEntry) {
        CorporationContractsRequest request = new CorporationContractsRequest(apiEntry.getCorpId());
        request.putParam("keyID", apiEntry.getKeyId());
        request.putParam("vCode", apiEntry.getvCode());
        return request;
    }
}
