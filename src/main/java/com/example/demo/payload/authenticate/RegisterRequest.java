package com.example.demo.payload.authenticate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private Long storeId;

}