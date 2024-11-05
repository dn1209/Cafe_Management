package com.example.demo.payload.request.bill;

import lombok.Data;

import java.util.List;
@Data
public class BillRequest {
    private String notes;
    private String customerPay;
    private List<DetailBillRequest> detailBill;
    private String orderStatus;
}
