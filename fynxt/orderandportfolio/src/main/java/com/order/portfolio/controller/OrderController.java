package com.order.portfolio.controller;

import com.order.portfolio.dto.PlaceOrderRequest;
import com.order.portfolio.entity.Order;
import com.order.portfolio.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order placeOrder(
            @Valid @RequestBody PlaceOrderRequest request) {

        return orderService.placeOrder(request);
    }

    @PostMapping("/{orderId}/fill")
    public Order fillOrder(
            @PathVariable Long orderId) {

        return orderService.fillOrder(orderId);
    }

    @PostMapping("/{orderId}/cancel")
    public Order cancelOrder(
            @PathVariable Long orderId) {

        return orderService.cancelOrder(orderId);
    }
}