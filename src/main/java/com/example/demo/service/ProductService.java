package com.example.demo.service;


import com.example.demo.payload.request.ProductNewRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<?> addNewProduct(ProductNewRequest productNewRequest, HttpServletRequest request);
}
