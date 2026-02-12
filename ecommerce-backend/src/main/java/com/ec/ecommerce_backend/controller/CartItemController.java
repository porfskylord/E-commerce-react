package com.ec.ecommerce_backend.controller;

import com.ec.ecommerce_backend.dto.CartItemRequest;
import com.ec.ecommerce_backend.dto.CartItemUpdateRequest;
import com.ec.ecommerce_backend.model.CartItem;
import com.ec.ecommerce_backend.model.DeliveryOption;
import com.ec.ecommerce_backend.model.Product;
import com.ec.ecommerce_backend.repository.CartItemRepository;
import com.ec.ecommerce_backend.repository.DeliveryOptionRepository;
import com.ec.ecommerce_backend.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemRepository cartItemRepository;
    private final DeliveryOptionRepository deliveryOptionRepository;
    private final ProductRepository productRepository;

    @GetMapping("/cart-items")
    public List<Object> getAllCartItems(@RequestParam(required = false) String expand) {
        List<CartItem> cartItems = cartItemRepository.findAll();

        if("product".equals(expand)) {
            return cartItems.stream()
                    .map(cartItem -> Map.of(
                            "productId", cartItem.getProductId(),
                            "quantity", cartItem.getQuantity(),
                            "deliveryOptionId", cartItem.getDeliveryOptionId(),
                            "product", productRepository.findById(cartItem.getProductId())
                    ))
                    .collect(Collectors.toList());
        }

        return cartItems.stream()
                .map(cartItem -> Map.of(
                        "productId", cartItem.getProductId(),
                        "quantity", cartItem.getQuantity(),
                        "deliveryOptionId", cartItem.getDeliveryOptionId()
                ))
                .collect(Collectors.toList());

    }

    @GetMapping("/payment-summary")
    public Map<String, Object> getCartTotals() {
        List<CartItem> cartItems = cartItemRepository.findAll();
        
        int totalItems = 0;
        int productCostCents = 0;
        int shippingCostCents = 0;

        for (CartItem item : cartItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElse(null);
            DeliveryOption deliveryOption = deliveryOptionRepository.findById(item.getDeliveryOptionId())
                    .orElse(null);
            
            if (product != null && deliveryOption != null) {
                totalItems += item.getQuantity();
                productCostCents += product.getPriceCents() * item.getQuantity();
                shippingCostCents += deliveryOption.getPriceCents();
            }
        }

        int totalCostBeforeTaxCents = productCostCents + shippingCostCents;
        int taxCents = Math.round(totalCostBeforeTaxCents * 0.1f);
        int totalCostCents = totalCostBeforeTaxCents + taxCents;

        return Map.of(
                "totalItems", totalItems,
                "productCostCents", productCostCents,
                "shippingCostCents", shippingCostCents,
                "totalCostBeforeTaxCents", totalCostBeforeTaxCents,
                "taxCents", taxCents,
                "totalCostCents", totalCostCents
        );
    }

    @PostMapping("/cart-items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartItem createCartItem(@Valid @RequestBody CartItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found"));

        DeliveryOption deliveryOption = deliveryOptionRepository.findAll().stream()
                .filter(option -> option.getDeliveryDays() == 7)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Delivery option not found"));

        Optional<CartItem> existingCartItem = cartItemRepository.findAll().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        CartItem cartItem;

        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setDeliveryOptionId(deliveryOption.getId());
        }

        return cartItemRepository.save(cartItem);
    }

    @PutMapping("/cart-items/{productId}")
    public CartItem updateCartItem(
            @PathVariable String productId,
            @Valid @RequestBody CartItemUpdateRequest request
    ) {
        Optional<CartItem> existingCartItem = cartItemRepository.findAll().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingCartItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found");
        }

        CartItem cartItem = existingCartItem.get();

        if (request.getQuantity() != null) {
            cartItem.setQuantity(request.getQuantity());
        }

        if (request.getDeliveryOptionId() != null) {
            if (!deliveryOptionRepository.existsById(request.getDeliveryOptionId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid delivery option");
            }
            cartItem.setDeliveryOptionId(request.getDeliveryOptionId());
        }

        return cartItemRepository.save(cartItem);
    }

    @DeleteMapping("/cart-items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        Optional<CartItem> existingCartItem = cartItemRepository.findAll().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingCartItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found");
        }

        CartItem cartItem = existingCartItem.get();
        cartItemRepository.delete(cartItem);
    }
}
