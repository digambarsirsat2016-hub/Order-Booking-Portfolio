package com.order.portfolio.dto;

public class BasketOverlapDto {

    private String basket;

    private String overlap;

    public BasketOverlapDto() {
    }

    public BasketOverlapDto(
            String basket,
            String overlap) {

        this.basket = basket;
        this.overlap = overlap;
    }

    public String getBasket() {
        return basket;
    }

    public void setBasket(String basket) {
        this.basket = basket;
    }

    public String getOverlap() {
        return overlap;
    }

    public void setOverlap(String overlap) {
        this.overlap = overlap;
    }
}
