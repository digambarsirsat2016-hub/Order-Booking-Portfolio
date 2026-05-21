package com.order.portfolio.service;

import com.order.portfolio.dto.PlaceOrderRequest;
import com.order.portfolio.entity.Order;
import com.order.portfolio.entity.OrderSide;
import com.order.portfolio.entity.OrderStatus;
import com.order.portfolio.entity.Portfolio;
import com.order.portfolio.exception.BusinessException;
import com.order.portfolio.exception.ResourceNotFoundException;
import com.order.portfolio.repository.OrderRepository;
import com.order.portfolio.repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PortfolioRepository portfolioRepository;

    public OrderService(
            OrderRepository orderRepository,
            PortfolioRepository portfolioRepository) {

        this.orderRepository = orderRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional
    public Order placeOrder(
            PlaceOrderRequest request) {

        long pendingOrders =
                orderRepository.countByTraderIdAndStatus(
                        request.getTraderId(),
                        OrderStatus.PENDING);

        if (pendingOrders >= 3) {
            throw new BusinessException(
                    "Trader already has 3 pending orders");
        }

        if (request.getSide() == OrderSide.SELL) {

            Portfolio portfolio =
                    portfolioRepository
                            .findByTraderIdAndStock(
                                    request.getTraderId(),
                                    request.getStock())
                            .orElseThrow(() ->
                                    new BusinessException(
                                            "No holdings available"));

            if (portfolio.getQuantity()
                    < request.getQuantity()) {

                throw new BusinessException(
                        "Insufficient shares");
            }
        }

        Order order = new Order();

        order.setTraderId(request.getTraderId());
        order.setStock(request.getStock());
        order.setSector(request.getSector());
        order.setQuantity(request.getQuantity());
        order.setSide(request.getSide());
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    @Transactional
    public Order fillOrder(Long orderId) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException(
                    "Only pending orders can be filled");
        }

        Portfolio portfolio =
                portfolioRepository
                        .findByTraderIdAndStock(
                                order.getTraderId(),
                                order.getStock())
                        .orElseGet(() -> {

                            Portfolio p = new Portfolio();
                            p.setTraderId(order.getTraderId());
                            p.setStock(order.getStock());
                            p.setSector(order.getSector());
                            p.setQuantity(0);

                            return p;
                        });

        if (order.getSide() == OrderSide.BUY) {

            portfolio.setQuantity(
                    portfolio.getQuantity()
                            + order.getQuantity());

        } else {

            if (portfolio.getQuantity()
                    < order.getQuantity()) {

                throw new BusinessException(
                        "Insufficient shares");
            }

            portfolio.setQuantity(
                    portfolio.getQuantity()
                            - order.getQuantity());
        }

        portfolioRepository.save(portfolio);

        order.setStatus(OrderStatus.FILLED);

        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException(
                    "Only pending orders can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);

        return orderRepository.save(order);
    }
}
