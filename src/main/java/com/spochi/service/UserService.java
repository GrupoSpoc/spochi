package com.spochi.service;

import com.spochi.dto.UserResponseDTO;
import com.spochi.entity.User;
import com.spochi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository repository;

    public UserResponseDTO findByGoogleId(String googleId) {
        return repository.findByGoogleId(googleId)
                .map(User::toDTO)
                .orElse(null);
    }
}
