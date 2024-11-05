package com.example.demo.payload.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class DetailBillResponse {
    private long detailBillId;

    private Long productId;

    private String productName;

    private int quantity;

    private BigDecimal price;
}
