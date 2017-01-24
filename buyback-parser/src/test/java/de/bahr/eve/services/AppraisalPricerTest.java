package de.bahr.eve.services;

import de.bahr.eve.services.AppraisalPricer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class AppraisalPricerTest {

    AppraisalPricer sut = new AppraisalPricer();

    @Test
    public void priceContract() throws Exception {
        String link1 = "https://skyblade.de/e/5904";
        Map<String, Double> expectation1 = new HashMap<>();
        expectation1.put("jitaBuy", 174443780.97);
        expectation1.put("jitaSell", 180469413.03);
        expectation1.put("buybackBuy", 174443780.97 * 0.97);

        Map<String, Double> result1 = sut.priceContract(link1);

        assertEquals(expectation1.get("jitaBuy"), result1.get("jitaBuy"));
        assertEquals(expectation1.get("jitaSell"), result1.get("jitaSell"));
        assertEquals(expectation1.get("buybackBuy"), result1.get("buybackBuy"));

        String link2 = "https://skyblade.de/e/15865";
        Map<String, Double> expectation2 = new HashMap<>();
        expectation2.put("jitaBuy", 9500000.04);
        expectation2.put("jitaSell", 11030111.0);
        expectation2.put("buybackBuy", 9500000.04 * 0.97);

        Map<String, Double> result2 = sut.priceContract(link2);

        assertEquals(expectation2.get("jitaBuy"), result2.get("jitaBuy"));
        assertEquals(expectation2.get("jitaSell"), result2.get("jitaSell"));
        assertEquals(expectation2.get("buybackBuy"), result2.get("buybackBuy"));
    }

}