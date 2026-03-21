package com.distributed.transaction_system.controller;

import com.distributed.transaction_system.service.UserService;
import org.springframework.web.bind.annotation.RestController;

import com.distributed.transaction_system.entity.User;
import com.distributed.transaction_system.entity.Wallet;
import com.distributed.transaction_system.repository.WalletRepository;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final WalletRepository walletRepository;

    UserController(UserService userService,
            WalletRepository walletRepository) {
        this.userService = userService;
        this.walletRepository = walletRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<User> postMethodName(@RequestBody String username) {
        User newUser = userService.createUser(username);

        // return 201 created status
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newUser);
    }

    @GetMapping("/wallets")
    public ResponseEntity<List<Wallet>> getWallets(@RequestBody Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);

        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(wallets);
    }

}
