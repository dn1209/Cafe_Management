package com.example.demo.serviceImp;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.*;
import com.example.demo.payload.request.ProductFilterRequest;
import com.example.demo.payload.request.ProductNewRequest;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticateService;
import com.example.demo.service.ProductService;
import com.example.demo.service.StoreService;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    AuthenticateService authenticateService;

    @Override
    public ResponseEntity<?> addNewProduct(ProductNewRequest productNewRequest, HttpServletRequest request) {
        Product product = new Product();
        Category category = findCategoryById(productNewRequest.getCategoryId());
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.CATEGORY_NOT_FOUND);
        }
        Store store = findStoreById(authenticateService.getStoreIdByUserId(request));
        if (store == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.STORE_NOT_FOUND);
        }
        product.setCategoryId(category.getCategoryId());
        product.setStoreId(store.getStoreId());
        addNewProduct(productNewRequest, product);
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.OK).body(Message.CREATE_PRODUCT_SUCCESS);
    }

    @Override
    public ResponseEntity<?> getProductList(ProductFilterRequest filter) {
        Specification<Product> spec = buildSpecification(filter);
        List<Product> productList = productRepository.findAll(spec);

        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @Override
    public ResponseEntity<?> getDetailProduct(Long id) {
        Product product = productRepository.findById(id).get();
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @Override
    public ResponseEntity<?> updateProduct(Long id, ProductNewRequest productNewRequest, HttpServletRequest request) {
        Product product = productRepository.findById(id).get();
        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.PRODUCT_NOT_FOUND);
        }
        Category category = findCategoryById(productNewRequest.getCategoryId());
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.CATEGORY_NOT_FOUND);
        }
        product.setCategoryId(category.getCategoryId());
        updateProduct(productNewRequest, product);
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.OK).body(Message.CREATE_PRODUCT_SUCCESS);
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long id) {
        Product product = productRepository.findById(id).get();
        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.PRODUCT_NOT_FOUND);
        }
        productRepository.deleteProductWithStatus(product.getProductId());
        return ResponseEntity.status(HttpStatus.OK).body(Message.CREATE_PRODUCT_SUCCESS);
    }

    private Specification<Product> buildSpecification(ProductFilterRequest filter) {return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (filter != null) {
                if (filter.getProductCd() != null) {
                    predicate = criteriaBuilder.and(
                            predicate,
                            criteriaBuilder.equal(root.get("productCd"), filter.getProductCd()));
                }

                if (filter.getStatusProduct() != null) {
                    predicate = criteriaBuilder.and(
                            predicate,
                            criteriaBuilder.equal(root.get("prdStatus"), filter.getStatusProduct()));
                }

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
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("prdStatus"), 1));

            query.orderBy(criteriaBuilder.asc(criteriaBuilder.toLong(root.get("productCd"))));

            return predicate;
        };
    }

    private void addNewProduct(ProductNewRequest productNewRequest, Product product) {
        LocalDate today = LocalDate.now();
        String a = "PD" + String.format("%05d", (productRepository.getLastProductId() + 1)); // Ví dụ mã PD00001, PD00002, ...
        product.setProductName(productNewRequest.getPrdName());
        product.setProductCd(a);
        product.setPrdSellPrice(BigDecimal.valueOf(productNewRequest.getPrdSellPrice()));
        product.setPrdOriginalPrice(BigDecimal.valueOf(productNewRequest.getPrdOriginPrice()));
        product.setNote(productNewRequest.getNote());
        product.setCreatedAt(today);
        product.setPrdStatus(1);
    }

    private void updateProduct(ProductNewRequest productNewRequest, Product product) {
        product.setProductName(productNewRequest.getPrdName());
        product.setPrdSellPrice(BigDecimal.valueOf(productNewRequest.getPrdSellPrice()));
        product.setPrdOriginalPrice(BigDecimal.valueOf(productNewRequest.getPrdOriginPrice()));
        product.setPrdStatus(productNewRequest.getStatus());
        product.setNote(productNewRequest.getNote());
    }

    public Category findCategoryById(Long id) {
        Optional<Category> gr = categoryRepository.findById(id);
        if (gr.isEmpty()) {
            throw new UserNotFoundException("Store not found with username: " + id);
        }
        Category grD = gr.get();
        return grD;
    }

    public Store findStoreById(Long id) {
        Optional<Store> storeOptional = storeRepository.findStoreById(id);
        if (storeOptional.isEmpty()) {
            throw new UserNotFoundException("Store not found with username: " + id);
        }
        Store store = storeOptional.get();
        return store;
    }

}
