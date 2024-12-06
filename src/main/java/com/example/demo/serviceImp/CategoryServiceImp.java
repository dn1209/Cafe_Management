package com.example.demo.serviceImp;

import com.example.demo.model.Category;
import com.example.demo.model.Message;
import com.example.demo.payload.request.category.CategoryRequest;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.AuthenticateService;
import com.example.demo.service.CategoryService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<?> createCategory(CategoryRequest categoryRequest) {
        Category cat = categoryRepository
                .findByCategoryName(categoryRequest.getCategoryName())
                .orElse(null);

        if (cat != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.CATEGORY_NAME_EXISTED));
        }

        Category category = new Category();
        category.setCategoryName(categoryRequest.getCategoryName());
        category.setCreatedDate(LocalDate.now());
        category.setStatus(1);
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(Message.map(Message.CREATE_CATEGORY_SUCCESS));
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
        Category category = categoryRepository.findById(id).orElse(null);

        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.CATEGORY_NOT_FOUND));
        }

        List<Category> categoryList = categoryRepository.findAll();
        for (Category categoryItem : categoryList) {
            if (Objects.equals(categoryItem.getCategoryName(), name) &&
                    !Objects.equals(categoryItem.getCategoryId(), id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.CATEGORY_NAME_EXISTED));
            }
        }

        category.setCategoryName(name);
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(Message.map(Message.UPDATE_CATEGORY_SUCCESS));
    }

    @Override
    public ResponseEntity<?> updateStatus(Long id) {
        Category category = categoryRepository.getReferenceById(id);

        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.CATEGORY_NOT_FOUND));
        }

        category.setStatus(category.getStatus() == 1 ? 0 : 1);
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(Message.map(Message.UPDATE_STATUS_SUCCESS));
    }

    private Specification<Category> buildSpecification() {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), 1));
            return predicate;
        };
    }
}
