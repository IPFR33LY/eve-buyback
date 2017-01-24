package de.bahr.eve;

import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.services.ApiLoader;
import de.bahr.eve.services.ApiProcessor;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class ApplicationTest extends BaseTest {

    Application sut = new Application();

    @Test
    public void testRun() {
        sut.apiLoader = mock(ApiLoader.class);
        List<ApiEntry> list = new ArrayList<>();
        list.add(getDummyApi());
        list.add(getDummyApi());
        when(sut.apiLoader.loadApis()).thenReturn(list);
        sut.apiProcessor = mock(ApiProcessor.class);

        sut.run();

        verify(sut.apiLoader).loadApis();

        ArgumentCaptor<ApiEntry> apiEntryCaptor = ArgumentCaptor.forClass(ApiEntry.class);
        verify(sut.apiProcessor, times(2)).process(apiEntryCaptor.capture());

        List<ApiEntry> capturedApiEntries = apiEntryCaptor.getAllValues();
        assertEquals("1", capturedApiEntries.get(0).getId());
        assertEquals("1", capturedApiEntries.get(1).getId());
    }
}
