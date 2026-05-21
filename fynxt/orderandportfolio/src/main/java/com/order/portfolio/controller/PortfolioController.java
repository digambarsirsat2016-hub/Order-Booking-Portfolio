package com.order.portfolio.controller;

import com.order.portfolio.dto.AddPortfolioRequest;
import com.order.portfolio.dto.OverlapResponse;
import com.order.portfolio.dto.PortfolioResponse;
import com.order.portfolio.service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(
            PortfolioService portfolioService) {

        this.portfolioService = portfolioService;
    }

    @GetMapping("/{traderId}")
    public PortfolioResponse getPortfolio(
            @PathVariable String traderId) {

        return portfolioService.getPortfolio(traderId);
    }

    @GetMapping("/{traderId}/overlap")
    public OverlapResponse getOverlapAnalysis(
            @PathVariable String traderId) {

        return portfolioService.getOverlapAnalysis(traderId);
    }

    @PostMapping("/{traderId}/holdings")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHolding(
            @PathVariable String traderId,
            @Valid @RequestBody AddPortfolioRequest request) {

        portfolioService.addHolding(traderId, request);
    }
}