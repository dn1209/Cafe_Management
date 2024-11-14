package com.example.demo.payload.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductNewRequest {
    private String prdName;

    private int prdSellPrice;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long storeId;

}
