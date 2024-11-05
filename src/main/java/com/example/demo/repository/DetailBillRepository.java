package com.example.demo.repository;

import com.example.demo.model.DetailBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailBillRepository extends JpaRepository<DetailBill, Long> {
    List<DetailBill> findByBillId(Long billId);
    List<DetailBill> findByBillIdIn(List<Long> billIds);
}
