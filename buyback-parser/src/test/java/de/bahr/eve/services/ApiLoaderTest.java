package de.bahr.eve.services;

import de.bahr.eve.BaseTest;
import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.repositories.ApiRepository;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class ApiLoaderTest extends BaseTest {

    private ApiLoader sut = new ApiLoader();

    @Test
    public void shouldProvideApiEntries() {
        // mock
        ApiEntry expected = getDummyApi();
        sut.repo = mock(ApiRepository.class);
        when(sut.repo.findAll()).thenReturn(Collections.singletonList(expected));

        List<ApiEntry> apis = sut.loadApis();

        ApiEntry actual = apis.get(0);
        assertEquals(expected.getId(),actual.getId());
    }
}
