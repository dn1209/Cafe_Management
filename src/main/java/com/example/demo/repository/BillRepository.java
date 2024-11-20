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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {
    @Query("SELECT b FROM Bill b WHERE b.storeId = ?1")
    Page<Bill> findByStoreId(Long storeId, Pageable pageable);

    @Modifying
    @Query("UPDATE Bill b set b.orderStatus = 0 WHERE b.billId = ?1")
    void updateBillsByBillId(Long bId);

    @Query("SELECT DATE(b.sellDate) as date, SUM(b.totalPrice) as revenue " +
            "FROM Bill b " +
            "WHERE b.sellDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(b.sellDate) " +
            "ORDER BY DATE(b.sellDate)")
    List<Object[]> calculateRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT FUNCTION('MONTH', b.sellDate) as month, SUM(b.totalPrice) as revenue FROM Bill b WHERE b.storeId = :storeId AND b.sellDate BETWEEN :startDate AND :endDate order by b.sellDate")
    List<Object[]> calculateRevenueByStore(@Param("storeId") Long storeId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Doanh thu của tất cả cửa hàng trong khoảng thời gian
    @Query("SELECT FUNCTION('DATE', b.sellDate) as date, b.storeId, SUM(b.totalPrice) as revenue FROM Bill b WHERE b.sellDate BETWEEN :startDate AND :endDate GROUP BY b.storeId")
    List<Object[]> calculateRevenueForAllStores(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT MONTH(b.sellDate) as month, SUM(b.totalPrice) as revenue " +
            "FROM Bill b " +
            "WHERE YEAR(b.sellDate) = YEAR(CURRENT_DATE) " +
            "GROUP BY MONTH(b.sellDate) " +
            "ORDER BY MONTH(b.sellDate)")
    List<Object[]> calculateMonthlyRevenueForCurrentYear();




}
