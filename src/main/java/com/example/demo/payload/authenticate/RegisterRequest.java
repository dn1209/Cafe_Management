package com.example.demo.payload.authenticate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String displayName;
    @NotNull
    private Long storeId;
    @NotNull
    private short userRole;
    @NotBlank
    private String userPhone;
}