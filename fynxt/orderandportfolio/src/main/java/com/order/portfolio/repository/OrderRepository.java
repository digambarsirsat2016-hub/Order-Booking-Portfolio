package com.order.portfolio.repository;

import com.order.portfolio.entity.Order;
import com.order.portfolio.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    long countByTraderIdAndStatus(
            String traderId,
            OrderStatus status);

    List<Order> findByTraderId(
            String traderId);
}