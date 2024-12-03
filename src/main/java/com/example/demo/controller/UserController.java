package com.example.demo.controller;

import com.example.demo.security.JwtTokenProvider;
import com.example.demo.payload.authenticate.LoginRequest;
import com.example.demo.payload.authenticate.RegisterRequest;
import com.example.demo.payload.request.user.ChangePasswordUser;
import com.example.demo.payload.request.user.UserUpdateRequest;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
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
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        return userService.registerUser(registerRequest);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> getAllUsers() {

        return userService.getAllUsers();
    }

    @PostMapping("/registerHidden")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest registerRequest) {

        return userService.registerAdmin(registerRequest);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable Long id,
                                            @Valid @RequestBody ChangePasswordUser changePasswordUser,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //logger.error(bindingResult.getAllErrors().toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        return userService.changePassword(id, changePasswordUser);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @Valid @RequestBody UserUpdateRequest userUpdateRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //logger.error(bindingResult.getAllErrors().toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }
        return userService.updateUser(id, userUpdateRequest);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailUser (@PathVariable Long id) {

        return userService.detailUser(id);
    }

    @GetMapping("/checking-register")
    public ResponseEntity<?> getCheckingUser () {
        return userService.checkingRegister();
    }
}
