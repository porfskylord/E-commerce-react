package com.ec.ecommerce_backend.dto;

import com.ec.ecommerce_backend.model.Rating;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {
    @NotNull(message = "Image Path is required")
    private String image;
    @NotNull(message = "Product name is required")
    private String name;
    @NotNull(message = "Rating is required")
    private Rating rating;
    @NotNull(message = "Price is required")
    private Integer priceCents;
    @NotNull(message = "Keywords are required")
    private List<String> keywords;
}
