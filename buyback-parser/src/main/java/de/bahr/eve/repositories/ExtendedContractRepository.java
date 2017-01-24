package de.bahr.eve.repositories;

import de.bahr.eve.entities.ExtendedContract;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public interface ExtendedContractRepository extends MongoRepository<ExtendedContract, String> {
    List<ExtendedContract> findByCorpId(String corpId);

    void deleteByContractId(Long contractId);
}
