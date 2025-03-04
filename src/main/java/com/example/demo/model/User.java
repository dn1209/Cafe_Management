package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_status")
    private int userStatus;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "user_role")
    private Short userRole;

    @Column(name = "logined")
    private LocalDate logined;

    public User(String userName,
                String password,
                int userStatus,
                LocalDate createdAt,
                LocalDate updatedAt,
                LocalDate logined) {
        this.userName = userName;
        this.password = password;
        this.userStatus = userStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.logined = logined;

    }


}