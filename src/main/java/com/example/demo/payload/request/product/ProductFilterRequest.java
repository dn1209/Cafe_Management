package com.example.demo.payload.request.product;

import lombok.Data;

@Data
public class ProductFilterRequest {
    private Long categoryId;

    private String keyword;

}
