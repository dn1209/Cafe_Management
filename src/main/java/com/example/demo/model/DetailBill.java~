package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "detail_bill")
@Data
public class DetailBill {
    @Id
    @SequenceGenerator(
            name = "detailBill_sequence",
            sequenceName = "detailBill_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "detailBill_sequence"
    )
    @Column(name = "detail_bill_id")
    private long detailBillId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price", columnDefinition = "numeric(15, 0)")
    private BigDecimal price;

    @Column(name = "bill_id")
    private Long billId;
}