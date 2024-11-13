package com.example.demo.payload.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
public class BillResponse {
    private long billId;

    private String sellDate;

    private String notes;

    private BigDecimal totalPrice;

    private BigDecimal customerPay;

    private int totalQuantity;

    private BigDecimal changeAmount;

    private String saleName;

    private int orderStatus;

    private List<DetailBillResponse> detailBill;
}
