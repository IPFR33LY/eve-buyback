package de.bahr.eve.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.bahr.eve.entities.QuantifiedItem;
import de.bahr.eve.services.AppraisalEstimator;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class AppraisalEstimatorTest {

    private AppraisalEstimator sut = new AppraisalEstimator();

    @Test
    public void createLink() throws Exception {
        QuantifiedItem tritanium = new QuantifiedItem("Tritanium", 123L, 1L);
        List<QuantifiedItem> items = Collections.singletonList(tritanium);

        String link = sut.createLink(items);

        assertTrue(link.startsWith("https://skyblade.de/e/"));
    }

    @Test
    public void extractAppraisalId() {
        String body = "\n" +
                "<script>\n" +
                "  document.title = \"Skypraisal - Result #15721 [Listing]\";\n" +
                "</script>\n";

        String appraisalId = sut.extractAppraisalId(body);
        assertEquals("15721", appraisalId);
    }

    @Test
    public void joinItems() {
        List<QuantifiedItem> list = new ArrayList<>();
        QuantifiedItem item1 = new QuantifiedItem("Tritanium", 123L, 1L);
        list.add(item1);
        QuantifiedItem item2 = new QuantifiedItem("Tritanium", 123L, 1L);
        list.add(item2);
        String expected = "Tritanium x1\nTritanium x1";

        String actual = sut.joinItems(list);

        assertEquals(expected, actual);
    }

    @Test
    public void unirestWorks() throws UnirestException {
        String url = "https://www.skyblade.de/e/5904";
        HttpResponse<String> stringHttpResponse = Unirest.get(url).asString();
        String body = stringHttpResponse.getBody();
        assertTrue(body.contains("Skypraisal - Result #5904"));
    }

    @Test
    public void unirestPostWorks() throws UnirestException {
        Map<String, Object> parameters = new HashMap<>();
        String items = "Tritanium x1";
        parameters.put("raw_paste", items);
        parameters.put("hide_buttons", "false");
        parameters.put("paste_autosubmit", "false");
        parameters.put("market", "30000142");
        parameters.put("save", "true");

        HttpResponse<String> stringHttpResponse = sut.unirestPost(parameters);
        String body = stringHttpResponse.getBody();
        assertTrue(body.contains("Skypraisal - Result #"));
    }

    @Test
    public void appraisalEstimate() throws UnirestException {
        String items = "Tritanium x1";
        HttpResponse<String> stringHttpResponse = sut.getAppraisalEstimate(items);
        String body = stringHttpResponse.getBody();
        assertTrue(body.contains("Skypraisal - Result #"));
    }

}