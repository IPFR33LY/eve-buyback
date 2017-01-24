package de.bahr.eve.services;

import com.tlabs.eve.api.corporation.CorporationContractsRequest;
import de.bahr.eve.BaseTest;
import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.services.ContractsLoader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class ContractsLoaderTest extends BaseTest {

    private ContractsLoader sut = new ContractsLoader();

    @Test
    public void buildRequest() throws Exception {
        ApiEntry api = getDummyApi();

        CorporationContractsRequest request = sut.buildRequest(api);

        assertEquals(api.getKeyId(), request.getParameters().get("keyID"));
        assertEquals(api.getvCode(), request.getParameters().get("vCode"));
        assertEquals(api.getCorpId(), request.getCorporationID());
    }

}