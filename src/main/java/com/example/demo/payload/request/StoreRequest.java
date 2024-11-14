package com.example.demo.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreRequest {
    @NotBlank
    private String storeName;
    @NotBlank
    private String storeAddress;
    @NotNull
    private int storeStatus;
}
