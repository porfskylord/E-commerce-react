package com.ec.ecommerce_backend.repository;

import com.ec.ecommerce_backend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
