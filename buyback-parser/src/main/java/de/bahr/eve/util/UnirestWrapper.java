package de.bahr.eve.util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Service;

/**
 * Created by michaelbahr on 24/01/2017.
 */
@Service
public class UnirestWrapper {
    public String getTextBody(String url) throws UnirestException {
        HttpResponse<String> response = Unirest.get(url).asString();
        if (response.getStatus() == 200) {
            return response.getBody();
        } else {
            throw new IllegalStateException("Unhandled status code: " + response.getStatus() + ", " + response.getStatusText());
        }
    }
}
