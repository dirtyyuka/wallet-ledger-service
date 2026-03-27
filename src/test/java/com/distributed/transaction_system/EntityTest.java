package com.distributed.transaction_system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.distributed.transaction_system.entity.TransactionRecord;
import com.distributed.transaction_system.entity.User;
import com.distributed.transaction_system.entity.Wallet;
import com.distributed.transaction_system.enums.WalletType;
import com.distributed.transaction_system.repository.UserRepository;
import com.distributed.transaction_system.repository.WalletRepository;
import com.distributed.transaction_system.service.PaymentService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EntityTest extends BasePostgresTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PaymentService paymentService;

    @Test
    void createAndValidateUser() {
        // create a user
        User user = new User();
        user.setUsername("jay");

        // save user
        User savedUser = userRepository.save(user);

        // get user
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);

        // validate
        assertNotNull(foundUser);
        assertEquals(savedUser.getUsername(), foundUser.getUsername());
    }

    @Test
    void addUserWallets() {
        // 1. Setup: Create the user needed for THIS test
        User user = new User();
        user.setUsername("jay");
        userRepository.save(user);

        // create wallet
        Wallet wallet = new Wallet();
        wallet.setAmount(BigDecimal.ZERO);
        wallet.setWalletType(WalletType.SAVINGS);

        // save wallet
        user.addWallet(wallet);
        Wallet savedWallet = walletRepository.save(wallet);

        // validate wallet
        Wallet foundWallet = walletRepository.findById(savedWallet.getId()).orElseThrow();
        assertNotNull(foundWallet);
        assertEquals(user.getId(), foundWallet.getUser().getId());
    }

    @Test
    void simulateTransaction() {
        // setup both users
        User user = new User();
        user.setUsername("jay");
        User savedUser1 = userRepository.save(user);

        User user2 = new User();
        user2.setUsername("dylan");
        User savedUser2 = userRepository.save(user2);

        // transaction record
        TransactionRecord txn = TransactionRecord.builder()
                .id(UUID.randomUUID().toString())
                .fromUserId(savedUser1.getId())
                .toUserId(savedUser2.getId())
                .amount(BigDecimal.valueOf(50L))
                .walletType(WalletType.CREDIT)
                .build();

        paymentService.saveTransfer(txn);

        // sleep for 5 secs
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS);

        assertNotNull(walletRepository.findByUserIdAndWalletType(savedUser2.getId(), WalletType.CREDIT));
    }
}