package com.example.demo.payload.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordUser {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
