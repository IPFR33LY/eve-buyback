package de.bahr.eve.services;

import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.repositories.ApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by michaelbahr on 24/01/2017.
 */
@Service
public class ApiLoader {

    @Autowired
    ApiRepository repo;

    public List<ApiEntry> loadApis() {
        return repo.findAll();
    }
}
