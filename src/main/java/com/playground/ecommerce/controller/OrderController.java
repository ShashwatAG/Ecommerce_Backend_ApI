package com.playground.ecommerce.controller;

import com.playground.ecommerce.dto.order.AdminUpdateOrderRequest;
import com.playground.ecommerce.dto.order.CheckoutRequest;
import com.playground.ecommerce.dto.order.OrderResponse;
import com.playground.ecommerce.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse checkout(Authentication authentication, @Valid @RequestBody CheckoutRequest request) {
        return orderService.checkout(authentication.getName(), request);
    }

    @GetMapping("/mine")
    public List<OrderResponse> getMyOrders(Authentication authentication) {
        return orderService.getMyOrders(authentication.getName());
    }

    @GetMapping("/mine/{orderId}")
    public OrderResponse getMyOrder(Authentication authentication, @PathVariable Long orderId) {
        return orderService.getMyOrder(authentication.getName(), orderId);
    }

    @GetMapping("/admin")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PatchMapping("/admin/{orderId}")
    public OrderResponse updateOrder(@PathVariable Long orderId, @RequestBody AdminUpdateOrderRequest request) {
        return orderService.updateOrder(orderId, request);
    }
}

