package com.order.portfolio.dto;

public class PositionDto {

    private String stock;
    private Integer quantity;

    public PositionDto() {
    }

    public PositionDto(String stock, Integer quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
