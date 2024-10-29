package com.example.demo.service;

import com.example.demo.payload.authenticate.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> registerUser(RegisterRequest registerRequest);


}
