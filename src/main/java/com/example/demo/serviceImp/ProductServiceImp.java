package com.example.demo.serviceImp;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Message;
import com.example.demo.model.Product;
import com.example.demo.payload.request.product.ProductFilterRequest;
import com.example.demo.payload.request.product.ProductNewRequest;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<?> addNewProduct(ProductNewRequest productNewRequest, HttpServletRequest request) {

        Category category = findCategoryById(productNewRequest.getCategoryId());
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.CATEGORY_NOT_FOUND));
        }

        Product product = new Product();
        if (productRepository.existsByName(productNewRequest.getPrdName())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.PRODUCT_NAME_EXISTED));
        }

        product.setCategoryId(category.getCategoryId());
        addNewProduct(productNewRequest, product);
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.OK).body(Message.map(Message.CREATE_PRODUCT_SUCCESS));
    }

    @Override
    public ResponseEntity<?> getProductList(ProductFilterRequest filter, Pageable pageable) {
        Specification<Product> spec = buildSpecification(filter, false, null);
        Page<Product> productList = productRepository.findAll(spec, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @Override
    public ResponseEntity<?> getProductListForUser(ProductFilterRequest filter, Pageable pageable) {
        List<Long> categoryList = categoryRepository.getCategoriesByStatus()
                .stream()
                .map(Category::getCategoryId)
                .toList();
        Specification<Product> spec = buildSpecification(filter, true, categoryList);
        Page<Product> productList = productRepository.findAll(spec, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    private Specification<Product> buildSpecification(
            ProductFilterRequest filter,
            boolean isForUser,
            List<Long> inactiveCategoryIds
    ) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filter != null) {
                if (filter.getCategoryId() != null) {
                    predicate = criteriaBuilder.and(
                            predicate,
                            criteriaBuilder.equal(root.get("categoryId"), filter.getCategoryId()));
                }
                if (filter.getKeyword() != null) {
                    predicate = criteriaBuilder.and(
                            predicate,
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("productName")),
                                    "%" + filter.getKeyword().toLowerCase() + "%"));
                }
            }
            if (isForUser) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("prdStatus"), 1));

            }
            if (inactiveCategoryIds != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.not(root.get("categoryId").in(inactiveCategoryIds))
                );
            }
            return predicate;
        };
    }

    @Override
    public ResponseEntity<?> getDetailProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.PRODUCT_NOT_FOUND));
        }

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @Override
    public ResponseEntity<?> updateProduct(Long id, ProductNewRequest productNewRequest) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.PRODUCT_NOT_FOUND));
        }

        if (productRepository.existsByName(productNewRequest.getPrdName()) &&
                !Objects.equals(product.getProductName(), productNewRequest.getPrdName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.PRODUCT_NAME_EXISTED));
        }

        Category category = findCategoryById(productNewRequest.getCategoryId());
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.CATEGORY_NOT_FOUND));
        }
        product.setCategoryId(category.getCategoryId());
        updateProduct(productNewRequest, product);
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.OK).body(Message.map(Message.UPDATE_PRODUCT_SUCCESS));
    }

    @Override
    public ResponseEntity<?> updateStatus(Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.PRODUCT_NOT_FOUND));
        }

        product.setPrdStatus(product.getPrdStatus() == 1 ? 0 : 1);
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.OK).body(Message.map(Message.UPDATE_PRODUCT_SUCCESS));
    }


    private void addNewProduct(ProductNewRequest productNewRequest, Product product) {
        LocalDate today = LocalDate.now();
        String a = "PD%s".formatted(String.format("%05d", (productRepository.getLastProductId() + 1))); // Ví dụ mã PD00001, PD00002, ...
        product.setProductName(productNewRequest.getPrdName());
        product.setProductCd(a);
        product.setPrdSellPrice(BigDecimal.valueOf(productNewRequest.getPrdSellPrice()));
        product.setCreatedAt(today);
        product.setPrdStatus(1);
    }

    private void updateProduct(ProductNewRequest productNewRequest, Product product) {
        product.setProductName(productNewRequest.getPrdName());
        product.setPrdSellPrice(BigDecimal.valueOf(productNewRequest.getPrdSellPrice()));
    }

    public Category findCategoryById(Long id) {
        Optional<Category> gr = categoryRepository.findById(id);
        if (gr.isEmpty()) {
            throw new UserNotFoundException("User with id: " + id + " is not found");
        }
        return gr.get();
    }

}
