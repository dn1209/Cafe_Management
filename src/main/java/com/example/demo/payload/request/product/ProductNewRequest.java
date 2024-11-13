package com.example.demo.payload.request.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductNewRequest {
    private String prdName;

    private int prdSellPrice;

    private Long categoryId;

    private String note;

    private int status;

    @NotBlank
    private Long storeId;

}
