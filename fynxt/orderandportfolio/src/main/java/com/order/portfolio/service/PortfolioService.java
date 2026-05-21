package com.order.portfolio.service;

import com.order.portfolio.dto.AddPortfolioRequest;
import com.order.portfolio.dto.OverlapResponse;
import com.order.portfolio.dto.PortfolioResponse;
import com.order.portfolio.entity.Portfolio;
import com.order.portfolio.repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final OverlapAnalysisService overlapAnalysisService;

    public PortfolioService(
            PortfolioRepository portfolioRepository,
            OverlapAnalysisService overlapAnalysisService) {

        this.portfolioRepository = portfolioRepository;
        this.overlapAnalysisService = overlapAnalysisService;
    }

    @Transactional
    public void addHolding(
            String traderId,
            AddPortfolioRequest request) {

        Portfolio portfolio =
                portfolioRepository
                        .findByTraderIdAndStock(
                                traderId,
                                request.getStock())
                        .orElseGet(() -> {

                            Portfolio p = new Portfolio();

                            p.setTraderId(traderId);
                            p.setStock(request.getStock());
                            p.setSector(request.getSector());
                            p.setQuantity(0);

                            return p;
                        });

        portfolio.setQuantity(
                portfolio.getQuantity()
                        + request.getQuantity());

        portfolioRepository.save(portfolio);
    }

    public PortfolioResponse getPortfolio(
            String traderId) {

        List<Portfolio> holdings =
                portfolioRepository.findByTraderId(traderId);

        Map<String, Integer> positions =
                new HashMap<>();

        Map<String, Integer> sectorBreakdown =
                new HashMap<>();

        for (Portfolio holding : holdings) {

            positions.put(
                    holding.getStock(),
                    holding.getQuantity());

            sectorBreakdown.merge(
                    holding.getSector(),
                    holding.getQuantity(),
                    Integer::sum);
        }

        PortfolioResponse response =
                new PortfolioResponse();

        response.setTraderId(traderId);
        response.setPositions(positions);
        response.setSectorBreakdown(sectorBreakdown);

        return response;
    }

    public OverlapResponse getOverlapAnalysis(
            String traderId) {

        List<Portfolio> holdings =
                portfolioRepository.findByTraderId(traderId);

        return overlapAnalysisService.analyze(holdings);
    }
}
