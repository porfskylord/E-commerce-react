package com.ec.ecommerce_backend.controller;

import com.ec.ecommerce_backend.dto.ProductRequest;
import com.ec.ecommerce_backend.model.Product;
import com.ec.ecommerce_backend.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    @GetMapping("/products")
    public List<Product> getAllProducts(@RequestParam(required = false) String search) {
        List<Product> products = productRepository.findAll();

        if (search != null && !search.trim().isEmpty()) {
            String lowerCaseSearch = search.toLowerCase();

            return products.stream()
                    .filter(product -> {
                        boolean nameMatch = product.getName() != null &&
                                product.getName().toLowerCase().contains(lowerCaseSearch);

                        boolean keywordsMatch = product.getKeywords() != null &&
                                product.getKeywords().stream()
                                        .anyMatch(keyword -> keyword != null &&
                                                keyword.toLowerCase().contains(lowerCaseSearch));

                        return nameMatch || keywordsMatch;
                    })
                    .toList();
        }

        return products;
    }

    @PostMapping("/products")
    public Product createProduct(@Valid @RequestBody ProductRequest request) {
        Product newProduct = new Product();
        newProduct.setImage(request.getImage());
        newProduct.setName(request.getName());
        newProduct.setRating(request.getRating());
        newProduct.setPriceCents(request.getPriceCents());
        newProduct.setKeywords(request.getKeywords());

        return productRepository.save(newProduct);
    }

    @PostMapping("/products/bulk")
    public List<Product> createProducts(@Valid @RequestBody List<ProductRequest> requests) {
        List<Product> newProducts = requests.stream()
                .map(request -> {
                    Product newProduct = new Product();
                    newProduct.setImage(request.getImage());
                    newProduct.setName(request.getName());
                    newProduct.setRating(request.getRating());
                    newProduct.setPriceCents(request.getPriceCents());
                    newProduct.setKeywords(request.getKeywords());
                    return newProduct;
                })
                .toList();

        return productRepository.saveAll(newProducts);
    }


}
