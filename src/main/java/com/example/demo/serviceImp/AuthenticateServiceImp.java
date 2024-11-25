package com.example.demo.serviceImp;

import com.example.demo.security.SecurityConfig;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticateServiceImp implements AuthenticateService {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public Long getUserIdByToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7); // Bỏ phần "Bearer " từ token
            Long userId = jwtTokenProvider.getUserIdFromJWT(token);
            return userId;
        }
        return null;
    }
}
