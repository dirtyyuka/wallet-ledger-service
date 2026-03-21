package com.distributed.transaction_system.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

import com.distributed.transaction_system.enums.OutboxStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String aggregateId; // transaction UUID

    @Column(nullable = false)
    private String type; // type of payment

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload; // transaction record json

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OutboxStatus status = OutboxStatus.PENDING;

    @CreationTimestamp
    private LocalDateTime createdAt; 

    private LocalDateTime processedAt;
}
