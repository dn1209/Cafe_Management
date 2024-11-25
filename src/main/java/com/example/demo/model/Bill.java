package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bill")
@Data
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private long billId;

    @Column(name = "sell_date" , columnDefinition = "DATETIME")
    private LocalDateTime sellDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "total_price", columnDefinition = "numeric(15, 0)")
    private BigDecimal totalPrice;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "sale_id")
    private Long saleId;

    @Column(name = "order_status")
    private int orderStatus;
}