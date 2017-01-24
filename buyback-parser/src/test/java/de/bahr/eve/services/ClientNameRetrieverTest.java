package de.bahr.eve.services;

import de.bahr.eve.services.ClientNameRetriever;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class ClientNameRetrieverTest {

    ClientNameRetriever sut = new ClientNameRetriever();

    @Test
    public void loadClientName() throws Exception {
        long clientId1 = 94993965;
        String result1 = sut.loadClientName(clientId1);
        assertEquals("Rihan Shazih", result1);

        long clientId2 = 94993966;
        String result2 = sut.loadClientName(clientId2);
        assertEquals("Marquess Victor", result2);
    }

}