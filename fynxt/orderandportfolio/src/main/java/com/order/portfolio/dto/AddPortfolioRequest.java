package com.order.portfolio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddPortfolioRequest {

    @NotBlank(message = "Stock is required")
    private String stock;

    @NotBlank(message = "Sector is required")
    private String sector;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
}
