package com.order.portfolio.service;

import com.order.portfolio.dto.BasketOverlapDto;
import com.order.portfolio.dto.OverlapResponse;
import com.order.portfolio.entity.Portfolio;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OverlapAnalysisService {

    private static final Map<String, Set<String>> BASKETS =
            Map.of(
                    "TECH_HEAVY",
                    Set.of(
                            "AAPL",
                            "MSFT",
                            "GOOGL",
                            "TSLA",
                            "NVDA"),

                    "FINANCE_HEAVY",
                    Set.of(
                            "JPM",
                            "GS",
                            "BAC",
                            "MS",
                            "WFC"),

                    "BALANCED",
                    Set.of(
                            "AAPL",
                            "JPM",
                            "XOM",
                            "JNJ",
                            "TSLA")
            );

    public OverlapResponse analyze(
            List<Portfolio> holdings) {

        Set<String> portfolioStocks =
                holdings.stream()
                        .map(Portfolio::getStock)
                        .collect(Collectors.toSet());

        List<BasketOverlapDto> overlaps =
                new ArrayList<>();

        double highestOverlap = 0;
        String dominantBasket = null;

        for (Map.Entry<String, Set<String>> entry :
                BASKETS.entrySet()) {

            Set<String> common =
                    new HashSet<>(portfolioStocks);

            common.retainAll(entry.getValue());

            double overlap =
                    ((2.0 * common.size())
                            /
                            (portfolioStocks.size()
                                    + entry.getValue().size()))
                            * 100;

            overlaps.add(
                    new BasketOverlapDto(
                            entry.getKey(),
                            String.format(
                                    "%.2f%%",
                                    overlap)));

            if (overlap > highestOverlap) {
                highestOverlap = overlap;
                dominantBasket = entry.getKey();
            }
        }

        String riskFlag;

        if (highestOverlap >= 60) {
            riskFlag = "HIGH";
        } else if (highestOverlap >= 40) {
            riskFlag = "MEDIUM";
        } else {
            riskFlag = "LOW";
        }

        OverlapResponse response =
                new OverlapResponse();

        response.setOverlaps(overlaps);
        response.setDominantBasket(dominantBasket);
        response.setRiskFlag(riskFlag);

        return response;
    }
}
