package com.example.demo.repository;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT u FROM Product u WHERE u.prdStatus = ?1 AND u.store.storeId = ?2 AND LOWER(u.productName) LIKE %?3%")
    List<Product> findProductsByStatusAndKeyword(int status, Long id, String keyword);

    @Query("SELECT u FROM Product u WHERE u.prdStatus = ?1 AND u.store.storeId = ?2 AND LOWER(u.productName) LIKE %?3% AND u.category.categoryId = ?4")
    List<Product> findProductsByStatusAndKeywordAndCategory(int status, Long sId, String keyword, Long id);

    @Query("SELECT u FROM Product u WHERE u.prdStatus = ?1 AND u.store.storeId = ?2 AND LOWER(u.productName) LIKE %?3% AND u.category.categoryId = ?4")
    List<Product> findProductsByStatusAndKeywordAndManu(int status, Long sId, String keyword, Long id);

    @Query(value = "SELECT COALESCE(MAX(id), 0) FROM Product", nativeQuery = true)
    Long getLastProductId();

    @Query("SELECT u from Product u WHERE u.productId = ?1 and  u.store.storeId = ?2")
    Optional<Product> findByIdAndStore(Long id, Long sId);

    @Query("SELECT u from Product u WHERE u.productId = ?1 and  u.store.storeId = ?2")
    Optional<Product> findByIdAndName(String name, Long sId);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM Product u WHERE u.productId = :id) THEN true ELSE false END")
    boolean existsById(Long id);

    //true neu ton tai va false neu khong ton tai
    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM Product u WHERE u.productName = :name) THEN true ELSE false END")
    boolean existsByName(String name);

    @Modifying
    @Query("UPDATE Product u set u.productName = ?1,u.productSls = ?2,u.prdOriginalPrice = ?3,u.prdSellPrice= ?4 , u.category = ?5,u.prdStatus = ?7 WHERE u.productId = ?8 ")
    void updateProductById(String prd_name, Float prd_sls, Float prdOriginalPrice,
                           Float prdSellPrice, Category category, int prdStatus, Long id);

    @Modifying
    @Query("UPDATE Product u set u.prdStatus = 0 WHERE u.productId = ?1 ")
    void updateProduct(Long id);
    @Modifying
    @Query("UPDATE Product u set u.prdStatus = 1 WHERE u.productId = ?1 ")
    void deleteProductWithStatus(Long id);
    @Modifying
    @Query("UPDATE Product u set u.productSls = u.productSls - ?1 WHERE u.productId = ?2 ")
    void updateProductWithPRDSLS(Float sl,Long id);
    @Query("SELECT CASE WHEN (SELECT u.productSls FROM Product u WHERE u.productId = :id) > :quantity THEN false ELSE true END")
    boolean checkProductWithIdAndQuantity(Long id, int quantity);
}
