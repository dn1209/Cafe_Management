package com.example.demo.repository;

import com.example.demo.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {

    @Query("SELECT DATE(b.sellDate) as date, SUM(b.totalPrice) as revenue " +
            "FROM Bill b " +
            "WHERE b.sellDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(b.sellDate) " +
            "ORDER BY DATE(b.sellDate)")
    List<Object[]> calculateRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT MONTH(b.sellDate) as month, SUM(b.totalPrice) as revenue " +
            "FROM Bill b " +
            "WHERE YEAR(b.sellDate) = YEAR(CURRENT_DATE) " +
            "GROUP BY MONTH(b.sellDate) " +
            "ORDER BY MONTH(b.sellDate)")
    List<Object[]> calculateMonthlyRevenueForCurrentYear();

}
