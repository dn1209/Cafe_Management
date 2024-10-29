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
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(name = "user_id")
    private long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "user_status")
    private String userStatus;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "logined")
    private LocalDate logined;

    @Column(name = "token_login")
    private String tokenLogin;

//    public User(String userName,
//                String password,
//                String email,
//                String userStatus,
//                LocalDate created,
//                LocalDate updated,
//                Store store,
//                LocalDate logined) {
//        this.userName = userName;
//        this.password = password;
//        this.email = email;
//        this.userStatus = userStatus;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.store = store;
//        this.logined = logined;
//    }

    public User(String userName,
                String password,
                String email,
                String userStatus,
                LocalDate createdAt,
                LocalDate updatedAt,
                Store store,
                LocalDate logined) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.userStatus = userStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.store = store;
        this.logined = logined;

    }
}