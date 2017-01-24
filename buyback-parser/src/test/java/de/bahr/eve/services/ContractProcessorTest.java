package de.bahr.eve.services;

import com.tlabs.eve.api.Contract;
import de.bahr.eve.BaseTest;
import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.entities.ExtendedContract;
import de.bahr.eve.entities.QuantifiedItem;
import de.bahr.eve.repositories.ExtendedContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class ContractProcessorTest extends BaseTest {

    ContractProcessor sut = new ContractProcessor();
    double HARD_CODED_BUYBACK_FACTOR = 0.97;
    double BUYBACK_PRICE = 10.0 * HARD_CODED_BUYBACK_FACTOR;

    @Before
    public void setUp() {
        sut.contractItemsLoader = mock(ContractItemsLoader.class);
        sut.appraisalEstimator = mock(AppraisalEstimator.class);
        sut.appraisalPricer = mock(AppraisalPricer.class);
        sut.clientNameRetriever = mock(ClientNameRetriever.class);
//        sut.locationRetriever = mock(LocationRetriever.class);
        sut.repo = mock(ExtendedContractRepository.class);
    }

    @Test
    public void parseContract() throws Exception {
        Contract contract = new Contract();
        List<QuantifiedItem> quantifiedItems = getDummyItems();
        ApiEntry apiEntry = getDummyApi();
        when(sut.contractItemsLoader.loadItems(contract, apiEntry)).thenReturn(quantifiedItems);
        when(sut.appraisalEstimator.createLink(quantifiedItems)).thenReturn("https://skyblade.de/e/123456");
        Map<String, Double> priceData = new HashMap<>();
        priceData.put("jitaSell", 12.0);
        priceData.put("jitaBuy", 10.0);
        priceData.put("buybackBuy", BUYBACK_PRICE);
        when(sut.appraisalPricer.priceContract("https://skyblade.de/e/123456")).thenReturn(priceData);
        when(sut.clientNameRetriever.loadClientName(contract.getIssuerID())).thenReturn("name");

        // run
        sut.parseContract(contract, apiEntry);

        // verify
        ExtendedContract expected = buildExpectedContract();
        ArgumentCaptor<ExtendedContract> repoInsertCaptor = ArgumentCaptor.forClass(ExtendedContract.class);
        verify(sut.repo).insert(repoInsertCaptor.capture());
        ExtendedContract result = repoInsertCaptor.getValue();

        assertEquals(-1, result.getContract().getContractID());
        assertEquals(expected.getLink(), result.getLink());
        assertEquals(expected.getClient(), result.getClient());
        assertTrue(expected.getBuybackBuy() == result.getBuybackBuy());
    }

    private ExtendedContract buildExpectedContract() {
        ExtendedContract result = getDummyContract();
        result.setQuantifiedItems(getDummyItems());
        result.setLink("https://skyblade.de/e/123456");
        result.setJitaSell(12.0);
        result.setJitaBuy(10.0);
        result.setBuybackBuy(BUYBACK_PRICE);
        result.setClient("name");
        return result;
    }

    private List<QuantifiedItem> getDummyItems() {
        return Collections.singletonList(new QuantifiedItem("itemName", 123L, 1L));
    }

}