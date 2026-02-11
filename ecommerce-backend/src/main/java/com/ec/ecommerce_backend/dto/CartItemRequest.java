package com.ec.ecommerce_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequest {
    @NotNull(message = "Product ID is required")
    private String productId;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 10, message = "Quantity cannot exceed 10")
    private Integer quantity;
}
