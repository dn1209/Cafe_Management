package com.example.demo.service;

import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService {
    @Autowired
    private UserRepository userRepository;

    public CustomUserDetails loadUserByUserId(Long id) throws Exception {
        User user = userRepository.findUserById(id);

        if (user == null) {
            throw new Exception("User not found");
        }
        return CustomUserDetails.mapToStoreDetail(user,"");
    }
}
