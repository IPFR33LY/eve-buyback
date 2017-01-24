package de.bahr.eve.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.bahr.eve.entities.QuantifiedItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michaelbahr on 24/01/2017.
 */
@Service
public class AppraisalEstimator {
    public String createLink(List<QuantifiedItem> quantifiedItems) {
        String items = joinItems(quantifiedItems);
        HttpResponse<String> appraisalEstimate;
        try {
            appraisalEstimate = getAppraisalEstimate(items);
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
        String body = appraisalEstimate.getBody();
        String appraisalId = extractAppraisalId(body);
        if (null == appraisalId) {
            return null;
        } else {
            return "https://skyblade.de/e/" + appraisalId;
        }
    }

    String extractAppraisalId(String body) {
        String titleRow = body.split("\n")[2];
        // example: document.title = "Skypraisal - Result #15721 [Listing]";
        String[] split = titleRow.split("Result #");
        if (split.length == 2) {
            String afterResult = split[1];
            return afterResult.split(" \\[Listing]")[0];
        } else {
            return null;
        }
    }

    String joinItems(List<QuantifiedItem> quantifiedItems) {
        List<String> itemsWithQuantities = new ArrayList<>();
        for (QuantifiedItem item : quantifiedItems) {
            itemsWithQuantities.add(item.getTypeName() + " x" + item.getQuantity());
        }
        return String.join("\n", itemsWithQuantities);
    }

    HttpResponse<String> unirestPost(Map<String, Object> parameters) throws UnirestException {
        return Unirest.post("https://skyblade.de/estimate").fields(parameters).asString();
    }

    HttpResponse<String> getAppraisalEstimate(String items) throws UnirestException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("raw_paste", items);
        parameters.put("hide_buttons", "false");
        parameters.put("paste_autosubmit", "false");
        parameters.put("market", "30000142");
        parameters.put("save", "true");

        return unirestPost(parameters);
    }
}
