package com.example.demo.payload.request;

import lombok.Data;

@Data
public class ProductFilterRequest {
    private Long productCd;

    private Integer statusProduct;

    private Long categoryId;

    private String keyword;
}
