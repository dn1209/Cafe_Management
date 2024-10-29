package com.example.demo.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductNewRequest {
    private String prd_name;
    private String prd_code;
    @NotBlank
    private int prd_sls;
    private int prd_origin_price;
    private int prd_sell_price;
    private Long category_id;

}
