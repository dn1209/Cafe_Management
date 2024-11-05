package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.payload.request.CategoryRequest;
import com.example.demo.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getCategporyList () {
        return categoryService.getCategoryList();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody String name) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.INVALID_CATEGORY_NAME);
        }
        return categoryService.updateCategory(id, name);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

}
