package com.ec.ecommerce_backend.controller;

import com.ec.ecommerce_backend.dto.DeliveryOptionRequest;
import com.ec.ecommerce_backend.model.DeliveryOption;
import com.ec.ecommerce_backend.repository.DeliveryOptionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DeliveryOptionController {
    private final DeliveryOptionRepository deliveryOptionRepository;

    @GetMapping("/delivery-options")
    public List<Object> getAllDeliveryOptions(@RequestParam(required = false) String expand) {
        List<DeliveryOption> options = deliveryOptionRepository.findAll();

        if ("estimatedDeliveryTime".equals(expand)) {
            return options.stream()
                    .map(option -> Map.of(
                            "id", option.getId(),
                            "deliveryDays", option.getDeliveryDays(),
                            "priceCents", option.getPriceCents(),
                            "estimatedDeliveryTimeMs", System.currentTimeMillis() + option.getDeliveryDays() * 24 * 60 * 60 * 1000L
                    ))
                    .collect(Collectors.toList());
        }

        return options.stream()
                .map(option -> Map.of(
                        "id", option.getId(),
                        "deliveryDays", option.getDeliveryDays(),
                        "priceCents", option.getPriceCents()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping("/delivery-options")
    public DeliveryOption createDeliveryOption(@Valid @RequestBody DeliveryOptionRequest request) {
        return deliveryOptionRepository.save(DeliveryOption.builder()
                .deliveryDays(request.getDeliveryDays())
                .priceCents(request.getPriceCents())
                .build());
    }


}
