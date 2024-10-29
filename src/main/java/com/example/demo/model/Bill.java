package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "bill_code")
    private String billCode;

    @Column(name = "sell_date")
    private LocalDate sellDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "payment_method")
    private int paymentMethod;

    @Column(name = "total_price", columnDefinition = "numeric(15, 0)")
    private BigDecimal totalPrice;

    @Column(name = "total_origin_price", columnDefinition = "numeric(15, 0)")
    private BigDecimal totalOriginPrice;

    @Column(name = "coupon")
    private int coupon;

    @Column(name = "customer_pay", columnDefinition = "numeric(15, 0)")
    private BigDecimal customerPay;

    @Column(name = "total_money", columnDefinition = "numeric(15, 0)")
    private BigDecimal totalMoney;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "change_amount", columnDefinition = "numeric(15, 0)")
    private BigDecimal changeAmount;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<DetailBill> detailBills;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "order_status")
    private int orderStatus;

    @Column(name = "created_at")
    private LocalDate createdAt;


}