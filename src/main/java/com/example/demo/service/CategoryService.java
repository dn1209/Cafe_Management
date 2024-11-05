package com.example.demo.service;

import com.example.demo.payload.request.CategoryRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<?> createCategory(CategoryRequest categoryRequest, HttpServletRequest request);

    ResponseEntity<?> getCategoryList();

    ResponseEntity<?> updateCategory(Long id, String name);

    ResponseEntity<?> deleteCategory(Long id);
}
