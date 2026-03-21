package com.distributed.transaction_system.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

import com.distributed.transaction_system.enums.WalletType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRecord {
    
    @Id
    private String id; // UUID

    @Column(nullable = false)
    private Long fromUserId; // user intiating payment

    @Column(nullable = false)
    private Long toUserId; // user receiving payment

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletType walletType;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount; 
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
