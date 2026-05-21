package com.order.portfolio.dto;

import java.util.List;

public class OverlapResponse {

    private List<BasketOverlapDto> overlaps;

    private String dominantBasket;

    private String riskFlag;

    public List<BasketOverlapDto> getOverlaps() {
        return overlaps;
    }

    public void setOverlaps(
            List<BasketOverlapDto> overlaps) {

        this.overlaps = overlaps;
    }

    public String getDominantBasket() {
        return dominantBasket;
    }

    public void setDominantBasket(
            String dominantBasket) {

        this.dominantBasket = dominantBasket;
    }

    public String getRiskFlag() {
        return riskFlag;
    }

    public void setRiskFlag(String riskFlag) {
        this.riskFlag = riskFlag;
    }
}
