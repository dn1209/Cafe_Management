package com.example.demo.serviceImp;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.Store;
import com.example.demo.model.User;
import com.example.demo.payload.request.ProductNewRequest;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticateService;
import com.example.demo.service.ProductService;
import com.example.demo.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        Category category = findCategoryById(productNewRequest.getCategory_id());
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category not found");
        }
        Store store = findStoreById(authenticateService.getStoreIdByUserId(request));
        if (store == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Store not found");
        }
        product.setCategory(category);
        product.setStore(store);
        addNewProduct(productNewRequest, product);
        productRepository.save(product);
        return null;
    }

    private void addNewProduct(ProductNewRequest productNewRequest, Product product) {
        LocalDate today = LocalDate.now();
        String a =productNewRequest.getPrd_code();
        if(productNewRequest.getPrd_code().isEmpty()){
            a = "PD" + String.format("%05d", (productRepository.getLastProductId() +1)); // Ví dụ mã PD00001, PD00002, ...
        }
        product.setProductName(productNewRequest.getPrd_name());
        product.setProductCd(a);
        product.setProductSls(productNewRequest.getPrd_sls());
        product.setPrdSellPrice(BigDecimal.valueOf(productNewRequest.getPrd_sell_price()));
        product.setPrdOriginalPrice(BigDecimal.valueOf(productNewRequest.getPrd_origin_price()));

        product.setCreatedAt(today);
        product.setPrdStatus(0);
    }

    public Category findCategoryById(Long id){
        Optional<Category> gr = categoryRepository.findById(id);
        if( gr.isEmpty()){
            throw new UserNotFoundException("Store not found with username: " + id);
        }
        Category grD = gr.get();
        return grD;
    }

    public Store findStoreById(Long id) {
        Optional<Store> storeOptional = storeRepository.findStoreById(id);
        if( storeOptional.isEmpty()){
            throw new UserNotFoundException("Store not found with username: " + id);
        }
        Store store = storeOptional.get();
        return store;
    }

}
