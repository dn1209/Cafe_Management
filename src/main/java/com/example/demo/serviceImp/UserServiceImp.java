package com.example.demo.serviceImp;

import com.example.demo.SecurityConfig;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.model.Message;
import com.example.demo.model.Store;
import com.example.demo.model.User;
import com.example.demo.model.enums.UserRole;
import com.example.demo.payload.authenticate.LoginRequest;
import com.example.demo.payload.authenticate.LoginResponse;
import com.example.demo.payload.authenticate.RegisterRequest;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SecurityConfig securityConfig;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StoreRepository storeRepository;

    @Override
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        boolean userNameExists = userRepository.existsByUserName(registerRequest.getUsername());

        if (userNameExists) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.USER_ALREADY_EXISTS);
        }

        registerUser(registerRequest.getUsername(),registerRequest.getPassword(),findStoreByUserName(registerRequest.getDisplay_name()),registerRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(Message.CREATE_USER_SUCCESS);
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        User user = userRepository.findUserByUserName(loginRequest.getUsername());
        if (user == null) {
            //logger
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.USER_NOT_FOUND);
        }

        String password = loginRequest.getPassword();
        Store store = storeRepository.findStoreByUserName(loginRequest.getParentuser());
        if (store == null) {
            //logger
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.STORE_NOT_FOUND);
        }
        if (user.getUserName().equals(loginRequest.getUsername()) && !user.getUserStatus().equals("0")) {
            //logger
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.INVALID_USER);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            //logger.error(Messages.INVALID_PASSWORD);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.INVALID_PASSWORD);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        switch (user.getUserRole()) {
            case 0:
                authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.name()));
                break;

            case 1:
                authorities.add(new SimpleGrantedAuthority(UserRole.USER.name()));
                break;
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(user);
        List<String> listRoles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        LoginResponse loginResponse = new LoginResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getDisplayName(),
                user.getUserStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLogined(),
                jwt);

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    private void registerUser(String userName, String password, Long storeId, String email) {
        LocalDate today = LocalDate.now();
        User user = new User(
                userName,
                securityConfig.passwordEncoder().encode(password),
                email,
                "1",
                today,
                today,
                storeId,
                today
        );
        user.setUserRole((short) 1);
        userRepository.save(user);
    }

    private Long findStoreByUserName(String userName) {
        if(storeRepository.existsByStoreUserName(userName)){
            Store store = new Store(userName);
            storeRepository.save(store);
        }
        Store store = storeRepository.findStoreByUserName(userName);
        if( store== null){
            throw new UserNotFoundException("Store not found with username: " + userName);
        }
        return store.getStoreId();
    }
}
