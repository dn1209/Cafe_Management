package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.payload.request.category.CategoryRequest;
import com.example.demo.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api-category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/add-new")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest) {
        if (categoryRequest == null || categoryRequest.getCategoryName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.INVALID_CATEGORY_NAME);
        }

        return categoryService.createCategory(categoryRequest);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> getCategporyList () {

        return categoryService.getCategoryList();
    }

    @GetMapping("/list-for-user")
    public ResponseEntity<?> getCategoryListForUser () {

        return categoryService.getCategoryForUser();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        if (categoryRequest == null || categoryRequest.getCategoryName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.INVALID_CATEGORY_NAME);
        }
        return categoryService.updateCategory(id, categoryRequest.getCategoryName());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return categoryService.updateStatus(id);
    }
}
