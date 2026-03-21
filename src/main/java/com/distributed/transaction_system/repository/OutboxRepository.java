package com.distributed.transaction_system.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.distributed.transaction_system.entity.OutboxEvent;
import com.distributed.transaction_system.enums.OutboxStatus;

import jakarta.transaction.Transactional;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    
    List<OutboxEvent> findByProcessedFalse();

    @Modifying // update/delete operation
    @Transactional // req for modifying queries
    @Query("DELETE FROM OutboxEvent e where e.status = :status and e.processedAt < :threshold")
    int bulkDeleteByStatusAndProcessedAtBefore(
        @Param("status") OutboxStatus status,
        @Param("threshold") LocalDateTime threshold
    );
    
}
