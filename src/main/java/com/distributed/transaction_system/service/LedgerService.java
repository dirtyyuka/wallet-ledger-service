package com.distributed.transaction_system.service;

import com.distributed.transaction_system.repository.ProcessedEventRepository;
import com.distributed.transaction_system.repository.UserRepository;
import com.distributed.transaction_system.repository.WalletRepository;

import java.math.BigDecimal;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.distributed.transaction_system.entity.ProcessedEvent;
import com.distributed.transaction_system.entity.TransactionRecord;
import com.distributed.transaction_system.entity.Wallet;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LedgerService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public void processIncomingTransaction(TransactionRecord txn) {

        // check for duplicate UUID
        if (processedEventRepository.existsById(txn.getId())) {
            log.warn("Duplicate transaction detected: {}. Skipping.", txn.getId());
            return;
        }

        // update recipient balance
        Wallet wallet = walletRepository.findByUserIdAndWalletType(txn.getToUserId(), txn.getWalletType())
                .orElseGet(() -> {
                    log.info("adding new wallet for user:" + txn.getToUserId() + "of type:" + txn.getWalletType());
                    Wallet newWallet = new Wallet();
                    newWallet.setAmount(BigDecimal.ZERO);
                    newWallet.setWalletType(txn.getWalletType());
                    userRepository.getReferenceById(txn.getToUserId()).addWallet(newWallet);
                    return walletRepository.save(newWallet);
                });

        wallet.setAmount(wallet.getAmount().add(txn.getAmount()));
        processedEventRepository.save(ProcessedEvent.builder().aggregateId(txn.getId()).build());
    }
}