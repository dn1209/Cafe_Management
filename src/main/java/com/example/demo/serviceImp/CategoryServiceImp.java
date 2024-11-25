package com.example.demo.serviceImp;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Message;
import com.example.demo.payload.request.category.CategoryRequest;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.AuthenticateService;
import com.example.demo.service.CategoryService;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<?> createCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setCategoryName(categoryRequest.getCategoryName());
        category.setCreatedDate(LocalDate.now());
        category.setStatus(1);
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(Message.CREATE_CATEGORY_SUCCESS);
    }

    @Override
    public ResponseEntity<?> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(categoryList);
    }

    @Override
    public ResponseEntity<?> getCategoryForUser() {
        Specification<Category> spec = buildSpecification();
        List<Category> categoryList = categoryRepository.findAll(spec);

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
    public ResponseEntity<?> updateStatus(Long id) {
        Category category = categoryRepository.getById(id);
        if (category == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.CATEGORY_NOT_FOUND);
        }
        category.setStatus(category.getStatus() == 1 ? 0 : 1);
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(Message.UPDATE_STATUS_SUCCESS);
    }
    private Specification<Category> buildSpecification() {return (root, query, criteriaBuilder) -> {
        Predicate predicate = criteriaBuilder.conjunction();
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), 1));
        return predicate;
    };
    }

}
