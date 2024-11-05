package com.example.demo.payload.authenticate;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
public class LoginResponse {
    private long userId;

    private String userName;

    private String email;

    private String displayName;

    private String userStatus;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate logined;

    private String tokenLogin;

    public LoginResponse(long userId,
                         String userName,
                         String email,
                         String displayName,
                         String userStatus,
                         LocalDate createdAt,
                         LocalDate updatedAt,
                         LocalDate logined,
                         String tokenLogin) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.displayName = displayName;
        this.userStatus = userStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.logined = logined;
        this.tokenLogin = tokenLogin;
    }
}
