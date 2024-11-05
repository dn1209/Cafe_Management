package com.example.demo.serviceImp;

import com.example.demo.SecurityConfig;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticateServiceImp implements AuthenticateService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public Long getStoreIdByUserId(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7); // Bỏ phần "Bearer " từ token
            Long userId = jwtTokenProvider.getUserIdFromJWT(token);
            Optional<Long> storeId = userRepository.findStoreIdByUserId(userId);
            if(storeId.isEmpty()){
                throw new UserNotFoundException("khong tim thay sotreid ");
            }
            return storeId.get();
        }
        return null;
    }

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
