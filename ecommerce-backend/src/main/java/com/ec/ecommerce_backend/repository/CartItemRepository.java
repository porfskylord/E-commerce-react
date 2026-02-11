package com.ec.ecommerce_backend.repository;

import com.ec.ecommerce_backend.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends MongoRepository<CartItem, String> {
}
