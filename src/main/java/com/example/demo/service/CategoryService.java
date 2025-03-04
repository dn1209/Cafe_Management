package com.example.demo.service;

import com.example.demo.payload.request.category.CategoryRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<?> createCategory(CategoryRequest categoryRequest);

    ResponseEntity<?> getCategoryList();

    ResponseEntity<?> getCategoryForUser();

    ResponseEntity<?> updateCategory(Long id, String name);

    ResponseEntity<?> updateStatus(Long id);
}
