package com.example.demo.payload.authenticate;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
public class LoginResponse {
    private long userId;

    private String userName;

    private String displayName;

    private int userStatus;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate logined;

    private String tokenLogin;

    private String userRole;

    public LoginResponse(long userId,
                         String userName,
                         String displayName,
                         int userStatus,
                         LocalDate createdAt,
                         LocalDate updatedAt,
                         LocalDate logined,
                         String tokenLogin,
                         String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.displayName = displayName;
        this.userStatus = userStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.logined = logined;
        this.tokenLogin = tokenLogin;
        this.userRole = userRole;
    }
}
