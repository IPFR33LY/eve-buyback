package de.bahr.eve.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.ContractItem;
import com.tlabs.eve.api.corporation.CorporationContractItemsRequest;
import de.bahr.eve.BaseTest;
import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.entities.QuantifiedItem;
import de.bahr.eve.services.ContractItemsLoader;
import de.bahr.eve.util.TypeCache;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class ContractItemsLoaderTest extends BaseTest {

    ContractItemsLoader sut;

    @Before
    public void setUp() {
        sut = new ContractItemsLoader();
    }

    @Test
    public void loadItems() throws Exception {
        sut = mock(ContractItemsLoader.class);

        // prepare
        ContractItem contractItem = new ContractItem();
        contractItem.setTypeID(123L);
        contractItem.setTypeName("test");
        contractItem.setQuantity(4L);
        when(sut.loadItems(any(), any())).thenCallRealMethod();
        List<ContractItem> list = Collections.singletonList(contractItem);
        when(sut.getApiItems(any(), anyLong())).thenReturn(list);
        when(sut.getTypeName(anyLong())).thenReturn("Tritanium");

        // run
        List<QuantifiedItem> result = sut.loadItems(new Contract(), getDummyApi());

        // verify
        QuantifiedItem actual = result.get(0);
        assertTrue(contractItem.getQuantity() == actual.getQuantity());
    }

    @Test
    public void buildRequest() {
        ApiEntry api = getDummyApi();

        CorporationContractItemsRequest request = sut.buildRequest(api, 123L);

        assertEquals(api.getKeyId(), request.getParameters().get("keyID"));
        assertEquals(api.getvCode(), request.getParameters().get("vCode"));
        assertEquals(api.getCorpId(), request.getCorporationID());
        assertEquals(123L, request.getContractID());
    }

    @Test
    public void getTypeName_fromCrest() throws UnirestException {
        sut.typeCache = new TypeCache();

        String result = sut.getTypeName(37L);
        assertEquals("Isogen", result);

        String cachedTypeName = sut.typeCache.get(37L);
        assertEquals("Isogen", cachedTypeName);
    }

    @Test
    public void getTypeName_fromCache() throws UnirestException {
        sut.typeCache = new TypeCache();
        sut.typeCache.set("Isogen", 37L);

        String result = sut.getTypeName(37L);
        assertEquals("Isogen", result);
    }

}