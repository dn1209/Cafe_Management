package com.example.demo.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductNewRequest {
    private String prdName;

    private int prdOriginPrice;

    private int prdSellPrice;

    private Long categoryId;

    private String note;

    private int status;

}
