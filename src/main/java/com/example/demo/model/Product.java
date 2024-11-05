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
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_cd")
    private String productCd;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "prd_sell_price", columnDefinition = "numeric(15, 0)")
    private BigDecimal prdSellPrice;

    @Column(name = "prd_origin_price", columnDefinition = "numeric(15, 0)")
    private BigDecimal prdOriginalPrice;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "note")
    private String note;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "prd_status")
    private int prdStatus;

    @Column(name = "created_at")
    private LocalDate createdAt;

}