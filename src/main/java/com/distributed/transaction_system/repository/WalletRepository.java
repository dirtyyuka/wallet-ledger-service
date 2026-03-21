package com.distributed.transaction_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.distributed.transaction_system.entity.Wallet;
import com.distributed.transaction_system.enums.WalletType;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    
    Optional<Wallet> findByUserIdAndWalletType(Long userId, WalletType walletType);

    List<Wallet> findByUserId(Long userId);
}
