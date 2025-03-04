package com.example.demo.controller;

import com.example.demo.payload.request.bill.BillRequest;
import com.example.demo.service.BillService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-bill")
public class BillController {
    @Autowired
    private BillService billService;

    @PostMapping("/add-new")
    public ResponseEntity<?> addNew(@RequestBody BillRequest billRequest,
                                    HttpServletRequest request,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        return billService.addNew(billRequest, request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> getListBill(HttpServletRequest request, @PageableDefault(size = 12) Pageable pageable) {

        return billService.getBillList(request, pageable);
    }

    @GetMapping("/list-for-user")
    public ResponseEntity<?> getListBillForUser(HttpServletRequest request, @PageableDefault(size = 12) Pageable pageable) {

        return billService.getBillListForUser(request, pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue(@RequestParam(required = false) String date) {

        return billService.getMonthlyRevenue(date);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all-stores")
    public ResponseEntity<?> getRevenueForAllStores() {


        return billService.getRevenueForAllStores();
    }

}
