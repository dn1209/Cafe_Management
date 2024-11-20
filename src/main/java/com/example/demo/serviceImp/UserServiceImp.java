package com.example.demo.serviceImp;

import com.example.demo.model.Product;
import com.example.demo.payload.request.product.ProductFilterRequest;
import com.example.demo.payload.request.user.UserFilterRequest;
import com.example.demo.security.SecurityConfig;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.model.Message;
import com.example.demo.model.Store;
import com.example.demo.model.User;
import com.example.demo.model.enums.UserRole;
import com.example.demo.payload.authenticate.LoginRequest;
import com.example.demo.payload.authenticate.LoginResponse;
import com.example.demo.payload.authenticate.RegisterRequest;
import com.example.demo.payload.request.user.ChangePasswordUser;
import com.example.demo.payload.request.user.UserUpdateRequest;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    private final static LocalDate today = LocalDate.now();

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
        Optional<Store> storeOptional = storeRepository.findStoreById(registerRequest.getStoreId());
        if (storeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.STORE_NOT_FOUND);
        }
        Store store = storeOptional.get();

        registerUser(registerRequest, store.getStoreId());
        return ResponseEntity.status(HttpStatus.CREATED).body(Message.CREATE_USER_SUCCESS);
    }

    @Override
    public ResponseEntity<?> registerAdmin(RegisterRequest registerRequest) {
        if (userRepository.count() != 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.USER_ALREADY_EXISTS);
        }
        User user = new User();
        user.setUserName(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedAt(today);
        user.setUpdatedAt(today);
        user.setUserRole((short) 0);
        user.setUserStatus(1);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            User user = userRepository.findUserByUserName(loginRequest.getUsername());
            if (user == null) {
                //logger
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.USER_NOT_FOUND);
            }

            String password = loginRequest.getPassword();
            if (!user.getUserName().equals(loginRequest.getUsername())) {
                //logger
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.INVALID_USER);
            }

            if (!passwordEncoder.matches(password, user.getPassword())) {
                //logger.error(Messages.INVALID_PASSWORD);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.INVALID_PASSWORD);
            }

            if (user.getUserStatus() != 1) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.INVALID_USER);
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

            LocalDate today = LocalDate.now();
            user.setLogined(today);
            userRepository.save(user);
            String jwt = tokenProvider.generateToken(user);
            List<String> listRoles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());


            LoginResponse loginResponse = new LoginResponse(
                    user.getUserId(),
                    user.getUserName(),
                    user.getDisplayName(),
                    user.getUserStatus(),
                    user.getCreatedAt(),
                    user.getUpdatedAt(),
                    user.getLogined(),
                    jwt,
                    listRoles.get(0));

            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.INVALID_USER);
        }
    }

    @Override
    public ResponseEntity<?> getAllUsers(Long storeId) {
        Specification<User> spec = buildSpecification(storeId);
        List<User> users = userRepository.findAll(spec);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Override
    public ResponseEntity<?> changePassword(Long id, ChangePasswordUser changePasswordUser) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(changePasswordUser.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(changePasswordUser.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Message.CHANGE_PASSWORD_SUCCESS);
    }

    @Override
    public ResponseEntity<?> updateUser(Long id, UserUpdateRequest userUpdateRequest) {

        User user = userRepository.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.USER_NOT_FOUND);
        }

        user.setDisplayName(userUpdateRequest.getDisplayName());
        user.setUserName(userUpdateRequest.getUserName());
        user.setUserStatus(userUpdateRequest.getUserStatus());
        user.setUserRole(  userUpdateRequest.getUserRole());
        user.setStoreId(  userUpdateRequest.getStoreId());
        user.setUserPhone( userUpdateRequest.getUserPhone());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(Message.UPDATE_USER_SUCCESS);
    }

    @Override
    public ResponseEntity<?> detailUser(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.USER_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Override
    public ResponseEntity<?> checkingRegister() {
        if (userRepository.count() == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
    }

    private void registerUser(RegisterRequest registerRequest, Long storeId) {
        User user = new User(
                registerRequest.getUsername(),
                securityConfig.passwordEncoder().encode(registerRequest.getPassword()),
                1,
                today,
                today,
                storeId,
                today
        );
        user.setUserPhone(registerRequest.getUserPhone());
        user.setDisplayName(registerRequest.getDisplayName());
        user.setUserRole( registerRequest.getUserRole());
        userRepository.save(user);
    }

    private Specification<User> buildSpecification(Long storeId) {return (root, query, criteriaBuilder) -> {
        Predicate predicate = criteriaBuilder.conjunction();

        if (storeId != null) {

            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(root.get("storeId"), storeId));

        }
        query.orderBy(criteriaBuilder.asc(criteriaBuilder.toLong(root.get("storeId"))));

        return predicate;
    };
    }
}
