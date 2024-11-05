package com.example.demo.serviceImp;

import com.example.demo.model.Category;
import com.example.demo.model.Message;
import com.example.demo.payload.request.CategoryRequest;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.AuthenticateService;
import com.example.demo.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<?> createCategory(CategoryRequest name, HttpServletRequest request) {
        if (authenticateService.getStoreIdByUserId(request) == null) {
            //logger
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.STORE_NOT_FOUND);
        }
        Category category = new Category();
        category.setCategoryName(name.getCategoryName());
        category.setCreatedDate(LocalDate.now());
        category.setStoreId(authenticateService.getStoreIdByUserId(request));
        category.setStatus(1);
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(Message.CREATE_CATEGORY_SUCCESS);
    }

    @Override
    public ResponseEntity<?> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAllByStatus();

        return ResponseEntity.status(HttpStatus.OK).body(categoryList);
    }

    @Override
    public ResponseEntity<?> updateCategory(Long id, String name) {
        Category category = categoryRepository.getById(id);
        if (category == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.CATEGORY_NOT_FOUND);
        }

        category.setCategoryName(name);
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(Message.UPDATE_CATEGORY_SUCCESS);
    }

    @Override
    public ResponseEntity<?> deleteCategory(Long id) {
        Category category = categoryRepository.getById(id);
        if (category == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.CATEGORY_NOT_FOUND);
        }
        category.setStatus(0);
        return ResponseEntity.status(HttpStatus.OK).body(Message.DELETE_CATEGORY_SUCCESS);
    }
}
