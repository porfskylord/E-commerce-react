package com.ec.ecommerce_backend.repository;

import com.ec.ecommerce_backend.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
