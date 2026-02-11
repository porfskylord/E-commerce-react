package com.ec.ecommerce_backend.repository;

import com.ec.ecommerce_backend.model.DeliveryOption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryOptionRepository extends MongoRepository<DeliveryOption,String> {
}
