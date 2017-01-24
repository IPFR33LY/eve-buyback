package de.bahr.eve.util;

import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by michaelbahr on 24/01/2017.
 */
@Service
public class TypeCache {

    private HashMap<Long, String> data = new HashMap<>();

    public String get(long typeId) {
        return data.get(typeId);
    }

    public void set(String typeName, long typeId) {
        data.put(typeId, typeName);
    }
}
