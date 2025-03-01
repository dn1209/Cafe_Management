package com.example.demo.service;


import com.example.demo.payload.request.product.ProductFilterRequest;
import com.example.demo.payload.request.product.ProductNewRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<?> addNewProduct(ProductNewRequest productNewRequest, HttpServletRequest request);

    ResponseEntity<?> getProductList(ProductFilterRequest productFilterRequest, Pageable pageable);

    ResponseEntity<?> getProductListForUser(ProductFilterRequest productFilterRequest, Pageable pageable);

    ResponseEntity<?> getDetailProduct(Long id);

    ResponseEntity<?> updateProduct(Long id, ProductNewRequest productNewRequest);

    ResponseEntity<?> updateStatus(Long id);

}
