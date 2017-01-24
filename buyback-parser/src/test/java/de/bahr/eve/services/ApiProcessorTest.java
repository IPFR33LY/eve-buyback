package de.bahr.eve.services;

import com.tlabs.eve.api.Contract;
import de.bahr.eve.BaseTest;
import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.entities.ExtendedContract;
import de.bahr.eve.repositories.ExtendedContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class ApiProcessorTest extends BaseTest {
    ApiProcessor sut = new ApiProcessor();


    @Before
    public void setUp() {
        sut.contractsLoader = mock(ContractsLoader.class);
        sut.contractProcessor = mock(ContractProcessor.class);
    }

    @Test
    public void parserShouldCallNecessarySubSteps() {
        sut.repo = mock(ExtendedContractRepository.class);

        Contract contract = new Contract();
        contract.setType(Contract.Type.EXCHANGE.getValue());
        when(sut.contractsLoader.loadContracts(any())).thenReturn(Collections.singletonList(contract));
        when(sut.repo.findByCorpId(anyString())).thenReturn(new ArrayList<>());

        // run
        sut.process(getDummyApi());

        // verify
        verify(sut.repo).findByCorpId(anyString());

        ArgumentCaptor<ApiEntry> apiCaptor = ArgumentCaptor.forClass(ApiEntry.class);
        verify(sut.contractsLoader).loadContracts(apiCaptor.capture());
        List<ApiEntry> capturedApiEntries = apiCaptor.getAllValues();
        assertEquals("1", capturedApiEntries.get(0).getId());

        ArgumentCaptor<Contract> contractCaptor = ArgumentCaptor.forClass(Contract.class);
        verify(sut.contractProcessor).parseContract(contractCaptor.capture(), any());

        List<Contract> captureContract = contractCaptor.getAllValues();
        assertEquals(-1L, captureContract.get(0).getContractID());
    }

    @Test
    public void dontProcessIfNotItemExchange() {
        sut.repo = mock(ExtendedContractRepository.class);

        Contract contract = new Contract();
        contract.setType("COURIER");
        when(sut.contractsLoader.loadContracts(any())).thenReturn(Collections.singletonList(contract));
        when(sut.repo.findByCorpId(anyString())).thenReturn(new ArrayList<>());

        // run
        sut.process(getDummyApi());

        // verify
        verify(sut.repo).findByCorpId(anyString());

        ArgumentCaptor<ApiEntry> apiCaptor = ArgumentCaptor.forClass(ApiEntry.class);
        verify(sut.contractsLoader).loadContracts(apiCaptor.capture());
        List<ApiEntry> capturedApiEntries = apiCaptor.getAllValues();
        assertEquals("1", capturedApiEntries.get(0).getId());

        verify(sut.contractProcessor, times(0)).parseContract(any(), any());
    }

    @Test
    public void dontProcessIfCompletedAndProcessedBefore() {
        sut.repo = mock(ExtendedContractRepository.class);

        Contract contract = new Contract();
        contract.setType(Contract.Type.EXCHANGE.getValue());
        contract.setStatus(Contract.Status.COMPLETED_COMPLETED.getValue());
        when(sut.contractsLoader.loadContracts(any())).thenReturn(Collections.singletonList(contract));
        when(sut.repo.findByCorpId(anyString()))
                .thenReturn(Collections.singletonList(new ExtendedContract(contract, getDummyApi())));

        // run
        sut.process(getDummyApi());

        // verify
        verify(sut.repo).findByCorpId(anyString());

        ArgumentCaptor<ApiEntry> apiCaptor = ArgumentCaptor.forClass(ApiEntry.class);
        verify(sut.contractsLoader).loadContracts(apiCaptor.capture());
        List<ApiEntry> capturedApiEntries = apiCaptor.getAllValues();
        assertEquals("1", capturedApiEntries.get(0).getId());

        verify(sut.contractProcessor, times(0)).parseContract(any(), any());
    }

    @Test
    public void doProcessIfCompletedAndNotProcessedBefore() {
        sut.repo = mock(ExtendedContractRepository.class);

        Contract contract = new Contract();
        contract.setType(Contract.Type.EXCHANGE.getValue());
        contract.setStatus(Contract.Status.COMPLETED_COMPLETED.getValue());
        when(sut.contractsLoader.loadContracts(any())).thenReturn(Collections.singletonList(contract));
        when(sut.repo.findByCorpId(anyString())).thenReturn(Collections.emptyList());

        // run
        sut.process(getDummyApi());

        // verify
        verify(sut.repo).findByCorpId(anyString());

        ArgumentCaptor<ApiEntry> apiCaptor = ArgumentCaptor.forClass(ApiEntry.class);
        verify(sut.contractsLoader).loadContracts(apiCaptor.capture());
        List<ApiEntry> capturedApiEntries = apiCaptor.getAllValues();
        assertEquals("1", capturedApiEntries.get(0).getId());

        verify(sut.contractProcessor, times(1)).parseContract(any(), any());
    }
}
