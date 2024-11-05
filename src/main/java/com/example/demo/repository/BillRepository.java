package com.example.demo.repository;

import com.example.demo.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByStoreId(Long storeId);

    @Modifying
    @Query("UPDATE Bill b set b.orderStatus = 0 WHERE b.billId = ?1")
    void updateBillsByBillId(Long bId);

}
