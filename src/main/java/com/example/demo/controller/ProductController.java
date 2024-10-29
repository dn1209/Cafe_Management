package com.example.demo.controller;

import com.example.demo.payload.request.ProductNewRequest;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api_products")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductNewRequest productNewRequest,
                                        HttpServletRequest request) {

        return productService.addNewProduct(productNewRequest, request);
    }

//    @GetMapping("/products")
//    public ResponseEntity<?> getProductOrDetail(@RequestParam(required = false) Long id,
//                                                @RequestParam(required = false) String   status_products,
//                                                @RequestParam(required = false) String     group_product_id,
//                                                @RequestParam(required = false) String    manufacture_id,
//                                                @RequestParam(required = false) String         keyword,
//                                                HttpServletRequest request) {
//
//        // Kiểm tra xem có param id được truyền vào hay không
//        if (id != null) {
//            System.out.println("apii");
//
//            // Xử lý khi có id được truyền vào
//            if (!productService.checkProduct(id)) {
//                ApiResponseError response = new ApiResponseError(HttpStatus.UNAUTHORIZED.value(), "Sản phẩm không tồn tại vui lòng kiểm tra lại");
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//            }
//            ApiProductDetailResponse api = productService.getDetailProduct(id, userServices.getStoreIdByUserId(request));
//            System.out.println("apii");
//            return ResponseEntity.ok(api);
//        } else {
//            // Xử lý khi không có id được truyền vào
//            List<ProductMapped> products = productService.getProductByKeyword(
//                    Integer.parseInt(status_products),
//                    userServices.getStoreIdByUserId(request),
//                    keyword,
//                    group_product_id,
//                    manufacture_id);
//            ApiProductResponse apiProductResponse = new ApiProductResponse("0", products.size(), products);
//            return ResponseEntity.ok(apiProductResponse);
//        }
//}

}
