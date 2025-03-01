package com.example.demo.repository;

import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    @Query("SELECT u from Category u where u.categoryId = ?1")
    Optional<Category> findById(Long id);

    @Query("SELECT u from Category u where u.categoryName = ?1")
    Optional<Category> findByCategoryName(String name);

    @Query("SELECT u from  Category u where u.status = 0")
    List<Category> getCategoriesByStatus();
}
