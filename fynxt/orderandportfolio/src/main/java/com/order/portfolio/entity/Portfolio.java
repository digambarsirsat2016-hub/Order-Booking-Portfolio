package com.order.portfolio.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "portfolio",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "traderId",
                                "stock"
                        }
                )
        }
)
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String traderId;

    @Column(nullable = false)
    private String stock;

    @Column(nullable = false)
    private String sector;

    @Column(nullable = false)
    private Integer quantity;

    @Version
    private Long version;

    public Long getId() {
        return id;
    }

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getVersion() {
        return version;
    }
}