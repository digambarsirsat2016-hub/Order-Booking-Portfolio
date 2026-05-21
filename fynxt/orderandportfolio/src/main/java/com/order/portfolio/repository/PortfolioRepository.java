package com.order.portfolio.repository;

import com.order.portfolio.entity.Portfolio;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository
        extends JpaRepository<Portfolio, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Portfolio> findByTraderIdAndStock(
            String traderId,
            String stock);

    List<Portfolio> findByTraderId(String traderId);
}
