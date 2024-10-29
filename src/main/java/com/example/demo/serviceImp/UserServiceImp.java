package com.example.demo.serviceImp;

import com.example.demo.SecurityConfig;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Store;
import com.example.demo.model.User;
import com.example.demo.payload.authenticate.RegisterRequest;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SecurityConfig securityConfig;
    @Autowired
    private StoreRepository storeRepository;

    @Override
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        boolean userNameExists = userRepository.existsByUserName(registerRequest.getUsername());

        if (userNameExists) {
            String message = "Tài khoản đã tồn tại";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }

        registerUser(registerRequest.getUsername(),registerRequest.getPassword(),findStoreByUserName(registerRequest.getDisplay_name()),registerRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("ăng ký tài khoản thành công");
    }

    private void registerUser(String userName, String password, Store store, String email) {
        LocalDate today = LocalDate.now();
        User user = new User(
                userName,
                securityConfig.passwordEncoder().encode(password),
                email,
                "1",
                today,
                today,
                store,
                today
        );
        userRepository.save(user);
    }

    private Store findStoreByUserName(String userName) {
        if(storeRepository.existsByStoreUserName(userName)){
            Store store = new Store(userName);
            storeRepository.save(store);
        }
        Optional<Store> storeOptional = storeRepository.findStoreByUserName(userName);
        if( storeOptional.isEmpty()){
            throw new UserNotFoundException("Store not found with username: " + userName);
        }
        Store store = storeOptional.get();
        return store;
    }
}
