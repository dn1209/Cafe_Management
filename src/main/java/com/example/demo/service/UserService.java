package com.example.demo.service;

import com.example.demo.payload.authenticate.LoginRequest;
import com.example.demo.payload.authenticate.RegisterRequest;
import com.example.demo.payload.request.user.ChangePasswordUser;
import com.example.demo.payload.request.user.UserUpdateRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> registerUser(RegisterRequest registerRequest);

    ResponseEntity<?> registerAdmin (RegisterRequest registerRequest);

    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> changePassword(Long id, ChangePasswordUser changePasswordUser);

    ResponseEntity<?> updateUser(Long id, UserUpdateRequest userUpdateRequest);

    ResponseEntity<?> detailUser(Long id);

    ResponseEntity<?> checkingRegister();

}
