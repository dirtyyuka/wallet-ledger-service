package com.distributed.transaction_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.distributed.transaction_system.entity.User;
import com.distributed.transaction_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public User createUser(String username) {
        // check if unique username
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username taken");
        }

        User user = new User();
        user.setUsername(username);
        // wallet null by default
        return userRepository.save(user);
    }
}
