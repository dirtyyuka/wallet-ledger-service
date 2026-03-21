package com.distributed.transaction_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.distributed.transaction_system.entity.TransactionRecord;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionRecord, String> {
    
    // sql query - select * from transaction_records where from_user_id = ?
    List<TransactionRecord> findByFromUserId(Long fromUserId);

    List<TransactionRecord> findByFromUserIdAndAmountGreaterThan(Long fromUserId, BigDecimal amount);

    // JPQL queries
    @Query("SELECT t from TransactionRecord t WHERE t.amount > :minAmount ORDER BY t.createdAt DESC")
    List<TransactionRecord> findExpensiveTransactions(@Param("minAmount") BigDecimal minAmount);

    // native queries
    @Query(value = "SELECT * FROM transactions WHERE created_at > NOW() - INTERVAL '1 day'", nativeQuery = true)
    List<TransactionRecord> findTransactionFromLast24Hours();
}
