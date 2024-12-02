package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_cd")
    private String productCd;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "prd_sell_price", columnDefinition = "numeric(15, 0)")
    private BigDecimal prdSellPrice;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "prd_status")
    private int prdStatus;

    @Column(name = "created_at")
    private LocalDate createdAt;

}