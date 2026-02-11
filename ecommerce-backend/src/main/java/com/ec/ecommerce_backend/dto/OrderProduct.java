package com.ec.ecommerce_backend.dto;

import com.ec.ecommerce_backend.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {
    private String productId;
    private Integer quantity;
    private Long estimatedDeliveryTimeMs;
    private Product product;
}
