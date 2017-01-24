package de.bahr.eve.util;

import de.bahr.eve.util.TypeCache;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class TypeCacheTest {

    TypeCache sut = new TypeCache();

    @Test
    public void nonExisting_shouldReturnNull() {
        assertNull(sut.get(123L));
    }

    @Test
    public void setAndRetrieve() {
        sut.set("Isogen", 37L);

        String result = sut.get(37L);

        assertEquals("Isogen", result);
    }

}