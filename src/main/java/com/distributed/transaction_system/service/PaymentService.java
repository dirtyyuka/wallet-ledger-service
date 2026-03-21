package com.distributed.transaction_system.service;

import org.springframework.stereotype.Service;

import com.distributed.transaction_system.entity.OutboxEvent;
import com.distributed.transaction_system.entity.TransactionRecord;
import com.distributed.transaction_system.enums.OutboxStatus;
import com.distributed.transaction_system.repository.OutboxRepository;
import com.distributed.transaction_system.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void saveTransfer(TransactionRecord txn) {
        // save the transfer
        transactionRepository.save(txn);

        // convert to JSON string
        String jsonPayload = objectMapper.writeValueAsString(txn);

        // save to outbox
        OutboxEvent event = OutboxEvent.builder()
                .aggregateId(txn.getId())
                .type("PAYMENT_CREATED")
                .payload(jsonPayload)
                .status(OutboxStatus.PENDING)
                .build();

        outboxRepository.save(event);
    }
}