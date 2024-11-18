package com.example.demo.controller;

import com.example.demo.payload.request.product.ProductFilterRequest;
import com.example.demo.payload.request.product.ProductNewRequest;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api_products")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/add_new")
    public ResponseEntity<?> addProduct (@Valid @RequestBody ProductNewRequest productNewRequest,
                                        HttpServletRequest request) {

        return productService.addNewProduct(productNewRequest, request);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getProductList (@ModelAttribute ProductFilterRequest filter,
                                             Pageable pageable,
                                             HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authority = authentication.getAuthorities().iterator().next().getAuthority();
        // Logic xử lý khi người dùng là ADMIN
        boolean isForUser = !"ADMIN".equals(authority);

        return productService.getProductList(filter, pageable, isForUser, request);
    }

    @GetMapping("/list_for_user")
    public ResponseEntity<?> getProductListForUser (@ModelAttribute ProductFilterRequest filter,
                                                    Pageable pageable,
                                                    HttpServletRequest request) {
        return productService.getProductList(filter, pageable, true, request);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detailProduct (@PathVariable Long id) {
        return productService.getDetailProduct(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct (@PathVariable Long id,
                                             @RequestBody ProductNewRequest productNewRequest) {

        return productService.updateProduct(id, productNewRequest);
    }

    @PutMapping("/toggle_status/{id}")
    public ResponseEntity<?> updateStatus (@PathVariable Long id) {

        return productService.updateStatus(id);
    }

}
