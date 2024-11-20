package com.example.demo.repository;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("SELECT u FROM Product u WHERE u.prdStatus = ?1 AND u.storeId = ?2 AND LOWER(u.productName) LIKE %?3%")
    List<Product> findProductsByStatusAndKeyword(int status, Long id, String keyword);

    @Query("SELECT u FROM Product u WHERE u.prdStatus = ?1 AND u.storeId = ?2 AND LOWER(u.productName) LIKE %?3% AND u.categoryId = ?4")
    List<Product> findProductsByStatusAndKeywordAndCategory(int status, Long sId, String keyword, Long id);

    @Query("SELECT u FROM Product u WHERE u.prdStatus = ?1 AND u.storeId = ?2 AND LOWER(u.productName) LIKE %?3% AND u.categoryId = ?4")
    List<Product> findProductsByStatusAndKeywordAndManu(int status, Long sId, String keyword, Long id);

    @Query(value = "SELECT COALESCE(MAX(product_id), 0) FROM Product", nativeQuery = true)
    Long getLastProductId();

    @Query("SELECT u from Product u WHERE u.productId = ?1 and  u.storeId = ?2")
    Optional<Product> findByIdAndStore(Long id, Long sId);

    List<Product> findByProductIdInAndStoreId(List<Long> ids, Long sId);

    List<Product> findByProductIdIn(List<Long> ids);

    @Query("SELECT u from Product u WHERE u.productId = ?1 and  u.storeId = ?2")
    Optional<Product> findByIdAndName(String name, Long sId);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM Product u WHERE u.productId = :id) THEN true ELSE false END")
    boolean existsById(Long id);

    //true neu ton tai va false neu khong ton tai
    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM Product u WHERE u.productName = :name) THEN true ELSE false END")
    boolean existsByName(String name);

    @Modifying
    @Query("UPDATE Product u set u.prdStatus = 0 WHERE u.productId = ?1 ")
    void deleteProductWithStatus(Long id);
}
