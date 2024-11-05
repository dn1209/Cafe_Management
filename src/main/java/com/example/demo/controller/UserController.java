package com.example.demo.controller;

import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.payload.authenticate.LoginRequest;
import com.example.demo.payload.authenticate.RegisterRequest;
import com.example.demo.service.StoreService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //logger.error(bindingResult.getAllErrors().toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        return userService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        return userService.registerUser(registerRequest);
    }

    @GetMapping("/random")
    public ResponseEntity<?> randomStuff(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7); // Bỏ phần "Bearer " từ token
            boolean isValidToken = jwtTokenProvider.validateToken(token);

            if (isValidToken) {
                Long userId = jwtTokenProvider.getUserIdFromJWT(token);
                if (userId != null) {
                    // Xử lý userId ở đây, ví dụ: trả về userId trong ResponseEntity
                    return ResponseEntity.ok("User ID: " + userId);
                }
            } else {
                // Token không hợp lệ, trả về mã lỗi và thông điệp
                return ResponseEntity.status(200).body("error_code: 404, message: JWT không hợp lệ");
            }
        }
        return ResponseEntity.ok("loi");

    }
}
