package de.bahr.eve.repositories;

import de.bahr.eve.entities.ApiEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public interface ApiRepository extends MongoRepository<ApiEntry, String> {
}
