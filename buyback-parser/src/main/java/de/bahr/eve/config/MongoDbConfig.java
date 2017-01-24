package de.bahr.eve.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import static java.util.Collections.singletonList;

/**
 * Created by michaelbahr on 19/04/16.
 */
@Configuration
public class MongoDbConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Value("${MONGO_URL}")
    private String host;
    @Value("${MONGO_PORT}")
    private Integer port;
    @Value("${MONGO_DB}")
    private String database;
    @Value("${MONGO_USER}")
    private String username;
    @Value("${MONGO_PASS}")
    private String password;

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(singletonList(new ServerAddress(host, port)),
                singletonList(MongoCredential.createCredential(username, database, password.toCharArray())));
    }
}