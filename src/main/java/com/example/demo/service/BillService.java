package com.example.demo.service;

import com.example.demo.payload.request.bill.BillRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface BillService {
     ResponseEntity<?> addNew (BillRequest billRequest, HttpServletRequest request);

     ResponseEntity<?> getBillList ( HttpServletRequest request, Pageable pageable);

     ResponseEntity<?> getRevenue ();

     ResponseEntity<?> getRevenueByStore (Long storeId);

     ResponseEntity<?> getRevenueForAllStores ();

      ResponseEntity<?> getMonthlyRevenue(String date);
}
