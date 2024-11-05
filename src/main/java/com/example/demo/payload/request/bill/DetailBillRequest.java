package com.example.demo.payload.request.bill;

import lombok.Data;

import java.util.List;

@Data
public class DetailBillRequest {
    private long productId;

    private int quantity;


    public DetailBillRequest(long id, int quantity) {
        this.productId = id;
        this.quantity = quantity;

    }
}
