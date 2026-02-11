package com.ec.ecommerce_backend.controller;

import com.ec.ecommerce_backend.dto.OrderProduct;
import com.ec.ecommerce_backend.model.CartItem;
import com.ec.ecommerce_backend.model.DeliveryOption;
import com.ec.ecommerce_backend.model.Order;
import com.ec.ecommerce_backend.model.Product;
import com.ec.ecommerce_backend.repository.CartItemRepository;
import com.ec.ecommerce_backend.repository.DeliveryOptionRepository;
import com.ec.ecommerce_backend.repository.OrderRepository;
import com.ec.ecommerce_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final DeliveryOptionRepository deliveryOptionRepository;
    private final ProductRepository productRepository;

    @GetMapping("/orders")
    public List<Object> getAllOrders(@RequestParam(required = false) String expand) {
        List<Order> orders = orderRepository.findAll();

        orders.sort((o1, o2) -> Long.compare(o2.getOrderTimeMs(), o1.getOrderTimeMs()));

        if ("products".equals(expand)) {
            return orders.stream()
                    .map(order -> {
                        List<OrderProduct> products = order.getProducts().stream()
                                .map(product -> {
                                    OrderProduct orderProduct = new OrderProduct();
                                    orderProduct.setProductId(product.getId());
                                    orderProduct.setQuantity(1);
                                    orderProduct.setProduct(product);
                                    return orderProduct;
                                })
                                .collect(Collectors.toList());

                        return Map.of(
                                "id", order.getId(),
                                "orderTimeMs", order.getOrderTimeMs(),
                                "totalCostCents", order.getTotalCostCents(),
                                "products", products,
                                "createdAt", order.getCreatedAt(),
                                "updatedAt", order.getUpdatedAt()
                        );
                    })
                    .collect(Collectors.toList());
        }

        return orders.stream()
                .map(order -> Map.of(
                        "id", order.getId(),
                        "orderTimeMs", order.getOrderTimeMs(),
                        "totalCostCents", order.getTotalCostCents(),
                        "products", order.getProducts(),
                        "createdAt", order.getCreatedAt(),
                        "updatedAt", order.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder() {
        List<CartItem> cartItems = cartItemRepository.findAll();

        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        List<OrderProduct> products = cartItems.stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Product not found: " + cartItem.getProductId()));

                    DeliveryOption deliveryOption = deliveryOptionRepository.findById(cartItem.getDeliveryOptionId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Invalid delivery option: " + cartItem.getDeliveryOptionId()));

                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setProductId(cartItem.getProductId());
                    orderProduct.setQuantity(cartItem.getQuantity());
                    orderProduct.setEstimatedDeliveryTimeMs(
                            System.currentTimeMillis() + deliveryOption.getDeliveryDays() * 24L * 60 * 60 * 1000
                    );

                    return orderProduct;
                })
                .collect(Collectors.toList());

        int totalCostCents = products.stream()
                .mapToInt(orderProduct -> {
                    Product product = productRepository.findById(orderProduct.getProductId()).orElseThrow();
                    CartItem cartItem = cartItems.stream()
                            .filter(item -> item.getProductId().equals(orderProduct.getProductId()))
                            .findFirst()
                            .orElseThrow();
                    DeliveryOption deliveryOption = deliveryOptionRepository.findById(cartItem.getDeliveryOptionId()).orElseThrow();

                    return (product.getPriceCents() * orderProduct.getQuantity()) + deliveryOption.getPriceCents();
                })
                .sum();

        totalCostCents = (int) Math.round(totalCostCents * 1.1);

        Order order = new Order();
        order.setOrderTimeMs(System.currentTimeMillis());
        order.setTotalCostCents(totalCostCents);
        order.setProducts(products.stream()
                .map(orderProduct -> productRepository.findById(orderProduct.getProductId()).orElseThrow())
                .collect(Collectors.toList()));

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteAll();

        return savedOrder;
    }

    @GetMapping("/orders/{orderId}")
    public Object getOrderById(@PathVariable String orderId, @RequestParam(required = false) String expand) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if ("products".equals(expand)) {
            List<OrderProduct> products = order.getProducts().stream()
                    .map(product -> {
                        OrderProduct orderProduct = new OrderProduct();
                        orderProduct.setProductId(product.getId());
                        orderProduct.setQuantity(1);
                        orderProduct.setProduct(product);
                        return orderProduct;
                    })
                    .collect(Collectors.toList());

            return Map.of(
                    "id", order.getId(),
                    "orderTimeMs", order.getOrderTimeMs(),
                    "totalCostCents", order.getTotalCostCents(),
                    "products", products,
                    "createdAt", order.getCreatedAt(),
                    "updatedAt", order.getUpdatedAt()
            );
        }

        return order;
    }
}
