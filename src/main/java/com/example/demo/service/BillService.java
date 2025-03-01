package com.example.demo.service;

import com.example.demo.payload.request.bill.BillRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BillService {
     ResponseEntity<?> addNew (BillRequest billRequest, HttpServletRequest request);

     ResponseEntity<?> getBillList ( HttpServletRequest request, Pageable pageable);

     ResponseEntity<?> getBillListForUser ( HttpServletRequest request, Pageable pageable);

     ResponseEntity<?> getMonthlyRevenue(String date);

     ResponseEntity<?> getRevenueForAllStores ();

}
