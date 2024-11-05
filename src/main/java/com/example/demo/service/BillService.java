package com.example.demo.service;

import com.example.demo.payload.request.bill.BillRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.awt.print.Pageable;

public interface BillService {
     ResponseEntity<?> addNew(BillRequest billRequest, HttpServletRequest request);

     ResponseEntity<?> getBillList( HttpServletRequest request);

     ResponseEntity<?> deleteBill(Long id);
}
