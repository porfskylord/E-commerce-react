package com.ec.ecommerce_backend.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemUpdateRequest {
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    private String deliveryOptionId;
}
