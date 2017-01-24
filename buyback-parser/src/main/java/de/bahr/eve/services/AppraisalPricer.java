package de.bahr.eve.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michaelbahr on 24/01/2017.
 */
@Service
public class AppraisalPricer {

    double BUYBACK_FACTOR = 0.97;

    public Map<String, Double> priceContract(String link) {
        String jsonLink = link + ".json";
        HttpResponse<JsonNode> response;
        try {
            response = unirestGet(jsonLink);
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }

        JSONObject object = response.getBody().getObject();
        JSONObject totals = (JSONObject) object.get("totals");
        double buy = totals.getDouble("buy");
        double sell = totals.getDouble("sell");
        double buyback = buy * BUYBACK_FACTOR;

        return buildResult(buy, sell, buyback);
    }

    HashMap<String, Double> buildResult(double buy, double sell, double buyback) {
        HashMap<String, Double> result = new HashMap<>();
        result.put("jitaBuy", buy);
        result.put("jitaSell", sell);
        result.put("buybackBuy", buyback);
        return result;
    }

    HttpResponse<JsonNode> unirestGet(String link) throws UnirestException {
        return Unirest.get(link).asJson();
    }
}
