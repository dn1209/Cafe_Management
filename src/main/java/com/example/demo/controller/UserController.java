package com.example.demo.controller;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.payload.authenticate.LoginRequest;
import com.example.demo.payload.authenticate.RegisterRequest;
import com.example.demo.service.StoreService;
import com.example.demo.service.UserService;
import com.example.demo.serviceImp.UserDetailServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserDetailServiceImp userDetailServiceImp;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    @Autowired
    StoreService storeService;
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            try {
                if (!loginRequest.getParentuser().equals(userDetailServiceImp.getParentUser(userDetails.getUser().getUserId()))) {
                    throw new UserNotFoundException("User not found with username: " + loginRequest.getParentuser());
                }

            }catch (UserNotFoundException ux){
                System.out.println(userDetailServiceImp.getParentUser(userDetails.getUser().getUserId()));
                String message = "User not found with username: " ;
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
            }
            try{
                if(userDetailServiceImp.checkingUserStatus(userDetails.getUser().getUserId())){
                    throw new UserNotFoundException("Tài khoản chưa được xác minh ");
                }
            }catch (UserNotFoundException uxx){
                String message = "User not found with username: " ;
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
            }
            String jwt = jwtTokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
            com.example.demo.model.payload.UserData userData = new com.example.demo.model.payload.UserData((CustomUserDetails) authentication.getPrincipal(),
                    userDetailServiceImp.getParentUser(userDetails.getUser().getUserId()),
                    jwt);
            return ResponseEntity.status(HttpStatus.OK).body(userData);
        }catch (BadCredentialsException ex){
            String message = "Tài khoản hoặc mật khẩu không đúng";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){

        return userService.registerUser(registerRequest);
    }

    @GetMapping("/random")
    public ResponseEntity<?> randomStuff(HttpServletRequest request){
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
