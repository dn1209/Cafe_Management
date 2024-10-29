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
    private long productId;

    @Column(name = "product_cd")
    private String productCd;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_sls")
    private int productSls;

    @Column(name = "prd_sell_price", columnDefinition = "numeric(15, 0)")
    private BigDecimal prdSellPrice;

    @Column(name = "prd_origin_price", columnDefinition = "numeric(15, 0)")
    private BigDecimal prdOriginalPrice;

    @ManyToOne
    @JoinColumn(name = "category_id") // Tên cột khóa ngoại trong bảng Product
    private Category category;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "prd_status")
    private int prdStatus;

    @Column(name = "created_at")
    private LocalDate createdAt;

}