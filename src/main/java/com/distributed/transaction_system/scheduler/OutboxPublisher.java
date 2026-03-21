package com.distributed.transaction_system.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.distributed.transaction_system.entity.OutboxEvent;
import com.distributed.transaction_system.enums.OutboxStatus;
import com.distributed.transaction_system.repository.OutboxRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // method runs every 2 secs
    @Scheduled(fixedRate = 2000) 
    public void publishToKafka() {
        List<OutboxEvent> pendingEvents = outboxRepository.findByProcessedFalse();

        if (pendingEvents.isEmpty()) {
            return;
        }

        log.info("Found {} pending events to publish at Kafka", pendingEvents.size());

        for (OutboxEvent event: pendingEvents) {
            try {
                // send to kafka
                kafkaTemplate.send("wallet-transactions", event.getAggregateId(), event.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            updateStatus(event.getId());
                        } else {
                            log.error("Failed to publish event {}: {}", event.getId(), ex.getMessage());
                        }
                    });
            } catch (Exception ex) {
                log.error("Unexpected error during Kafka publishing for event {} {}", event.getId(), ex.getMessage()); 
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanupOldEvents() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);

        // single delete query
        int deletedCount = outboxRepository.bulkDeleteByStatusAndProcessedAtBefore(
            OutboxStatus.PROCESSED,
            threshold
        );

        log.info("Cleaned up {} processed outbox events.", deletedCount);
    }
    
    @Transactional
    public void updateStatus(Long id) {
        outboxRepository.findById(id).ifPresent(e -> {
            e.setStatus(OutboxStatus.PROCESSED);
            e.setProcessedAt(LocalDateTime.now());
            outboxRepository.save(e);
        });
    }
}
