package com.example.demo.service;

import com.example.demo.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
public interface AuthenticateService {

    public Long getUserIdByToken(HttpServletRequest request);
}
