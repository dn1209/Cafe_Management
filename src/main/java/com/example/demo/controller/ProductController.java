package com.example.demo.controller;

import com.example.demo.payload.request.product.ProductFilterRequest;
import com.example.demo.payload.request.product.ProductNewRequest;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-products")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    ProductService productService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/add-new")
    public ResponseEntity<?> addProduct (@Valid @RequestBody ProductNewRequest productNewRequest,
                                        HttpServletRequest request) {

        return productService.addNewProduct(productNewRequest, request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> getProductList (@ModelAttribute ProductFilterRequest filter,
                                             Pageable pageable) {

        return productService.getProductList(filter, pageable);
    }

    @GetMapping("/list-for-user")
    public ResponseEntity<?> getProductListForUser (@ModelAttribute ProductFilterRequest filter,
                                                    Pageable pageable) {

        return productService.getProductListForUser(filter, pageable);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detailProduct (@PathVariable Long id) {
        return productService.getDetailProduct(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct (@PathVariable Long id,
                                             @RequestBody ProductNewRequest productNewRequest) {

        return productService.updateProduct(id, productNewRequest);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<?> updateStatus (@PathVariable Long id) {

        return productService.updateStatus(id);
    }

}
