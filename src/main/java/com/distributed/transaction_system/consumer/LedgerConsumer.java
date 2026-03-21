package com.distributed.transaction_system.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.distributed.transaction_system.entity.TransactionRecord;
import com.distributed.transaction_system.service.LedgerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Component
@Slf4j
@RequiredArgsConstructor
public class LedgerConsumer {
    
    private final LedgerService ledgerService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "wallet-transactions", groupId = "ledger-service-group")
    public void consume(String message) {
        try {
            // parse the json payload
            TransactionRecord txn = objectMapper.readValue(message, TransactionRecord.class);

            log.info("Received transaction for processing {}", txn.getId());

            // delegate to transactional service for processing
            ledgerService.processIncomingTransaction(txn);
            
        } catch (Exception e) {
            log.error("Error processing Kafka message {}", e.getMessage());
        }
    }
}
