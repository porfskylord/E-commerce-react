package com.ec.ecommerce_backend.dto;

import lombok.Data;

@Data
public class DeliveryOptionRequest {
    private Integer deliveryDays;
    private Integer priceCents;
}
