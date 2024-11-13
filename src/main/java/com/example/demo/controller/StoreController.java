package com.example.demo.controller;

import com.example.demo.payload.request.StoreRequest;
import com.example.demo.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api_store")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> getAllStore() {
        return storeService.getAllStore();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> createStore(@RequestBody @Valid StoreRequest storeRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }
        return storeService.registerStore(storeRequest);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detailStore(@PathVariable Long id) {
        return storeService.detailStore(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStore(@PathVariable Long id, @RequestBody @Valid StoreRequest storeRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }
        return storeService.updateStore(id, storeRequest);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Long id) {
        return storeService.deleteStore(id);
    }
}
