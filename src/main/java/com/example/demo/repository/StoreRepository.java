package com.example.demo.repository;

import com.example.demo.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {
    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM Store s WHERE s.storeName = :storeUserName) THEN false ELSE true END")
    boolean existsByStoreUserName(String storeUserName);
//false neu da ton tai va true neu chua ton tai

    @Query("SELECT u FROM Store u WHERE u.storeName = ?1")
    Store findStoreByUserName(String userName);
    @Query("SELECT u FROM Store u WHERE u.storeId = ?1")
    Optional<Store> findStoreById(Long userId);
}