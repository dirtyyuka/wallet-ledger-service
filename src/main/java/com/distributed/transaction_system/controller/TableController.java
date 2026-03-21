package com.distributed.transaction_system.controller;

import com.distributed.transaction_system.repository.ProcessedEventRepository;
import com.distributed.transaction_system.repository.TransactionRepository;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.distributed.transaction_system.entity.ProcessedEvent;
import com.distributed.transaction_system.entity.TransactionRecord;

@RestController
@RequestMapping("/show")
public class TableController {

    private final TransactionRepository transactionRepository;
    private final ProcessedEventRepository processedEventRepository;

    TableController(TransactionRepository transactionRepository,
            ProcessedEventRepository processedEventRepository) {
        this.transactionRepository = transactionRepository;
        this.processedEventRepository = processedEventRepository;
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionRecord>> getTransactions() {
        List<TransactionRecord> transactions = transactionRepository.findAll();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(transactions);
    }

    @GetMapping("/events")
    public ResponseEntity<List<ProcessedEvent>> getEvents() {
        List<ProcessedEvent> events = processedEventRepository.findAll();
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(events);
    }
}
