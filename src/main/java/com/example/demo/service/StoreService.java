package com.example.demo.service;

import com.example.demo.model.Store;
import com.example.demo.payload.request.StoreRequest;
import org.springframework.http.ResponseEntity;

public interface StoreService {
    ResponseEntity<?> getAllStore();

    ResponseEntity<?> registerStore(StoreRequest storeRequest);

    ResponseEntity<?> detailStore(Long id);

    ResponseEntity<?> updateStore(Long id, StoreRequest storeRequest);

    ResponseEntity<?> deleteStore(Long id);

}
