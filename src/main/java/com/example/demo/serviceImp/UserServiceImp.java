package com.example.demo.serviceImp;

import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.model.enums.UserRole;
import com.example.demo.payload.authenticate.LoginRequest;
import com.example.demo.payload.authenticate.LoginResponse;
import com.example.demo.payload.authenticate.RegisterRequest;
import com.example.demo.payload.request.user.ChangePasswordUser;
import com.example.demo.payload.request.user.UserUpdateRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.security.SecurityConfig;
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
    private final static LocalDate today = LocalDate.now();

    @Autowired
    UserRepository userRepository;
    @Autowired
    SecurityConfig securityConfig;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        boolean userNameExists = userRepository.existsByUserName(registerRequest.getUserName());
        if (userNameExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.USER_ALREADY_EXISTS));
        }

        register(registerRequest);
        return ResponseEntity.status(HttpStatus.OK).body(Message.map(Message.CREATE_USER_SUCCESS));
    }

    @Override
    public ResponseEntity<?> registerAdmin(RegisterRequest registerRequest) {
        if (userRepository.count() != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.USER_ALREADY_EXISTS));
        }
        User user = new User();
        user.setUserName(registerRequest.getUserName());
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
            User user = userRepository.findUserByUserName(loginRequest.getUserName());
            if (user == null) {
                //logger
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.map(Message.USER_NOT_FOUND));
            }

            String password = loginRequest.getPassword();
            if (!user.getUserName().equals(loginRequest.getUserName())) {
                //logger
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.INVALID_USER));
            }

            if (!passwordEncoder.matches(password, user.getPassword())) {
                //logger.error(Messages.INVALID_PASSWORD);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.INVALID_PASSWORD));
            }

            if (user.getUserStatus() != 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.INVALID_USER));
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
                    .toList();

            LoginResponse loginResponse = new LoginResponse(
                    user.getUserId(),
                    user.getUserName(),
                    user.getDisplayName(),
                    jwt,
                    listRoles.getFirst()
            );

            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.INVALID_USER));
        }
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Override
    public ResponseEntity<?> changePassword(Long id, ChangePasswordUser changePasswordUser) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.map(Message.USER_NOT_FOUND));
        }
        if (!passwordEncoder.matches(changePasswordUser.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map(Message.INVALID_PASSWORD));
        }

        user.setPassword(passwordEncoder.encode(changePasswordUser.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Message.map(Message.CHANGE_PASSWORD_SUCCESS));
    }

    @Override
    public ResponseEntity<?> updateUser(Long id, UserUpdateRequest userUpdateRequest) {

        User user = userRepository.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.map(Message.USER_NOT_FOUND));
        }

        user.setDisplayName(userUpdateRequest.getDisplayName());
        user.setUserName(userUpdateRequest.getUserName());
        user.setUserStatus(userUpdateRequest.getUserStatus());
        user.setUserRole(userUpdateRequest.getUserRole());
        user.setUserPhone(userUpdateRequest.getUserPhone());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(Message.map(Message.UPDATE_USER_SUCCESS));
    }

    @Override
    public ResponseEntity<?> detailUser(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.map(Message.USER_NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Override
    public ResponseEntity<?> checkingRegister() {
        if (userRepository.count() == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
    }

    private void register(RegisterRequest registerRequest) {
        User user = new User(
                registerRequest.getUserName(),
                securityConfig.passwordEncoder().encode(registerRequest.getPassword()),
                1,
                today,
                today,
                today
        );
        user.setUserPhone(registerRequest.getUserPhone());
        user.setDisplayName(registerRequest.getDisplayName());
        user.setUserRole(registerRequest.getUserRole());
        userRepository.save(user);
    }
}
