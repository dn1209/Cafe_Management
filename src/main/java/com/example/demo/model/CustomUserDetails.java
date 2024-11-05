package com.example.demo.model;

import com.example.demo.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Data
@AllArgsConstructor
public class CustomUserDetails {
    private long userId;

    private String userName;

    private String password;

    private String email;

    private String displayName;

    private String userStatus;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate logined;

    private String tokenLogin;

    private Collection<? extends GrantedAuthority> authorities;

    public static CustomUserDetails mapToStoreDetail(User user, String accessToken) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        switch (user.getUserRole()) {
            case 0:
                authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.name()));
                break;
            case 1:
                authorities.add(new SimpleGrantedAuthority(UserRole.USER.name()));
                break;

        }
        return new CustomUserDetails(
                user.getUserId(),
                user.getUserName(),
                user.getPassword(),
                user.getEmail(),
                user.getDisplayName(),
                user.getUserStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLogined(),
                accessToken,
                authorities
        );
    }

}
