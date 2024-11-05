package com.example.demo.payload.request;

import lombok.Data;

@Data
public class ProductFilterRequest {
    private Long productCd;

    private String statusProduct;

    private String categoryId;

    private String keyword;
}
