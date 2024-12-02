package com.example.demo.payload.authenticate;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
public class LoginResponse {
    private long id;

    private String username;

    private String name;

    private int userStatus;

    private String expired;


    private String token;

    private String role;

    public LoginResponse(long userId,
                         String userName,
                         String displayName,
                         String tokenLogin,
                         String userRole) {
        this.id = userId;
        this.username = userName;
        this.name = displayName;
        this.role = userRole;
        this.token = tokenLogin;
        this.expired = "30 phút";
    }
}
