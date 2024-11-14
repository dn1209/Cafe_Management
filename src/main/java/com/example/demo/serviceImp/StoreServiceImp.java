package com.example.demo.serviceImp;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Message;
import com.example.demo.model.Store;
import com.example.demo.payload.request.StoreRequest;
import com.example.demo.repository.StoreRepository;
import com.example.demo.service.StoreService;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImp implements StoreService {
    private static final LocalDate today = LocalDate.now();
    @Autowired
    private StoreRepository storeRepository;


    @Override
    public ResponseEntity<?> getAllStore() {
        List<Store> storeList = storeRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(storeList);
    }

    @Override
    public ResponseEntity<?> registerStore(StoreRequest storeRequest) {
        boolean storeNameExists = storeRepository.existsByStoreUserName(storeRequest.getStoreName());
        if (storeNameExists) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.STORE_ALREADY_EXISTS);
        }
        Store store = new Store();
        registerUser(storeRequest,store);
        storeRepository.save(store);
        return ResponseEntity.status(HttpStatus.OK).body(Message.CREATE_STORE_SUCCESS);
    }

    @Override
    public ResponseEntity<?> detailStore(Long id) {
        Optional<Store> storeOptional = storeRepository.findStoreById(id);
        if (storeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.STORE_NOT_FOUND);
        }
        Store store = storeOptional.get();
        return ResponseEntity.status(HttpStatus.OK).body(store);
    }

    @Override
    public ResponseEntity<?> updateStore(Long id, StoreRequest storeRequest) {
        Optional<Store> storeOptional = storeRepository.findStoreById(id);
        if (storeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.STORE_NOT_FOUND);
        }
        Store store = storeOptional.get();
        store.setStoreUpdatedDate(today);
        store.setStoreAddress(storeRequest.getStoreAddress());
        store.setStoreName(storeRequest.getStoreName());
        store.setStoreStatus(storeRequest.getStoreStatus());
        storeRepository.save(store);
        return ResponseEntity.status(HttpStatus.OK).body(Message.UPDATE_STORE_SUCCESS);
    }

    @Override
    public ResponseEntity<?> deleteStore(Long id) {
        Optional<Store> storeOptional = storeRepository.findStoreById(id);
        if (storeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.STORE_NOT_FOUND);
        }
        Store store = storeOptional.get();
        store.setStoreStatus(store.getStoreStatus() == 1 ? 0 : 1);
        storeRepository.save(store);
        return ResponseEntity.status(HttpStatus.OK).body(Message.DELETE_STORE_SUCCESS);
    }

    public void registerUser (StoreRequest storeRequest, Store store) {
        store.setStoreAddress(storeRequest.getStoreAddress());
        store.setStoreName(storeRequest.getStoreName());
        store.setStoreStatus(storeRequest.getStoreStatus());
        store.setStoreUpdatedDate(today);
        store.setStoreCreatedDate(today);
    }

}
