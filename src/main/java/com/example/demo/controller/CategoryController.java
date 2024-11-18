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
@RequestMapping("/api_category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add_new")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest, HttpServletRequest request) {
        if (categoryRequest == null || categoryRequest.getCategoryName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.INVALID_CATEGORY_NAME);
        }

        return categoryService.createCategory(categoryRequest, request);
    }
    @GetMapping("/list")
    public ResponseEntity<?> getCategporyList (@RequestParam(required = false)  Long storeId, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authority = authentication.getAuthorities().iterator().next().getAuthority();
        // Logic xử lý khi người dùng là ADMIN
        boolean isForUser = !"ADMIN".equals(authority);
        return categoryService.getCategoryList(storeId, isForUser, request);
    }

    @GetMapping("/list_for_user")
    public ResponseEntity<?> getCategporyListForUser (@RequestParam(required = false)  Long storeId, HttpServletRequest request) {

        return categoryService.getCategoryList(storeId, true, request);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody String name) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.INVALID_CATEGORY_NAME);
        }
        return categoryService.updateCategory(id, name);
    }

    @PutMapping("/toggle_status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {

        return categoryService.updateStatus(id);
    }

}
