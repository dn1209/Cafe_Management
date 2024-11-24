package com.example.demo.controller;

import com.example.demo.payload.request.bill.BillRequest;
import com.example.demo.service.BillService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api_bill")
public class BillController {
    @Autowired
    private BillService billService;

    @PostMapping("/add_new")
    public ResponseEntity<?> addNew(@RequestBody BillRequest billRequest,
                                    HttpServletRequest request,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        return billService.addNew(billRequest, request);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListBill(@RequestParam(required = false) Long storeId, HttpServletRequest request, @PageableDefault(size = 12) Pageable pageable) {

        return billService.getBillList(request, pageable, false, storeId);
    }

    @GetMapping("/list_for_user")
    public ResponseEntity<?> getListBillForUser(HttpServletRequest request, @PageableDefault(size = 12) Pageable pageable) {

        return billService.getBillList(request, pageable, true, null);
    }

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue(@RequestParam(required = false) String date) {


        return billService.getMonthlyRevenue(date);
    }

    @GetMapping("/store")
    public ResponseEntity<?> getRevenueByStore(
            @RequestParam("storeId") Long storeId,
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {

        LocalDateTime startDate = LocalDateTime.parse(startDateStr);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr);

        return billService.getRevenueByStore(storeId);
    }

    @GetMapping("/all-stores")
    public ResponseEntity<?> getRevenueForAllStores() {


        return billService.getRevenueForAllStores();
    }


}
