package com.order.portfolio.dto;

import com.order.portfolio.entity.OrderSide;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaceOrderRequest {

    @NotBlank(message = "Trader ID is required")
    private String traderId;

    @NotBlank(message = "Stock is required")
    private String stock;

    @NotBlank(message = "Sector is required")
    private String sector;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Order side is required")
    private OrderSide side;
}