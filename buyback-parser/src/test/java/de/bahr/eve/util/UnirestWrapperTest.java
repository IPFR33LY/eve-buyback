package de.bahr.eve.util;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class UnirestWrapperTest {

    UnirestWrapper sut = new UnirestWrapper();

    @Test
    public void getTextBody() throws Exception {
        String url = "https://api.ipify.org";
        String result = sut.getTextBody(url);

        // 1.1.1.1 has the length 7
        assertTrue(result.length() >= 7);
    }

}