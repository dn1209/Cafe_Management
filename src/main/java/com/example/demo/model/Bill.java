package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bill")
@Data
public class Bill {
    @Id
    @SequenceGenerator(
            name = "orders_sequence",
            sequenceName = "orders_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "orders_sequence"
    )
    @Column(name = "bill_id")
    private long billId;

    @Column(name = "sell_date")
    private LocalDate sellDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "total_price", columnDefinition = "numeric(15, 0)")
    private BigDecimal totalPrice;

    @Column(name = "customer_pay", columnDefinition = "numeric(15, 0)")
    private BigDecimal customerPay;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "change_amount", columnDefinition = "numeric(15, 0)")
    private BigDecimal changeAmount;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "sale_id")
    private Long saleId;

    @Column(name = "order_status")
    private int orderStatus;
}