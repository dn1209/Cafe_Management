package com.example.demo.controller;

import com.example.demo.payload.request.ProductFilterRequest;
import com.example.demo.payload.request.ProductNewRequest;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> getProductOrDetail (@ModelAttribute ProductFilterRequest filter, Pageable pageable) {

            return productService.getProductList(filter, pageable);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detailProduct (@PathVariable Long id) {
        return productService.getDetailProduct(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct (@PathVariable Long id,
                                             @RequestBody ProductNewRequest productNewRequest,
                                        HttpServletRequest request) {

        return productService.updateProduct(id, productNewRequest, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct (@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

}
