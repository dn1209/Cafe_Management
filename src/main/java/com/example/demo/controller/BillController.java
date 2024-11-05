package com.example.demo.controller;

import com.example.demo.payload.request.bill.BillRequest;
import com.example.demo.service.BillService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api_bill")
public class BillController {
    @Autowired
    private BillService billService;

    @PostMapping("/add_new")
    public ResponseEntity<?> addNew (@RequestBody BillRequest billRequest,
                                     HttpServletRequest request,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        return billService.addNew(billRequest, request);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListBill(HttpServletRequest request) {

        return billService.getBillList(request);
    }

}
