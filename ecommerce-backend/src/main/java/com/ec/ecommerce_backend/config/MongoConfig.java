package com.ec.ecommerce_backend.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.Optional;

@Configuration
@EnableMongoAuditing

public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://127.0.0.1:27017/ecommerce_db");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "ecommerce_db");
    }

}
