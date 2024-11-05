package com.example.demo.payload.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Data
@Builder
public class BillResponse {
    private long billId;

    private LocalDate sellDate;

    private String notes;

    private BigDecimal totalPrice;

    private BigDecimal customerPay;

    private int totalQuantity;

    private BigDecimal changeAmount;

    private Long storeId;

    private Long saleId;

    private int orderStatus;

    private List<DetailBillResponse> detailBill;
}
