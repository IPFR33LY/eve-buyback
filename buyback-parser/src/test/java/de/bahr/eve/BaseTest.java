package de.bahr.eve;

import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.entities.ExtendedContract;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class BaseTest {
    protected ApiEntry getDummyApi() {
        return new ApiEntry("1", "2", "3", "1", "k", "123");
    }

    protected ExtendedContract getDummyContract() {
        return new ExtendedContract(getDummyApi());
    }
}
