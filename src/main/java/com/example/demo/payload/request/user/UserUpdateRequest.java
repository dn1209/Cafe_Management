package com.example.demo.payload.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank
    private String userName;

    @NotNull
    private long storeId;

    @NotNull
    private int userStatus;

    @NotNull
    private short userRole;

    @NotBlank
    private String displayName;

}
