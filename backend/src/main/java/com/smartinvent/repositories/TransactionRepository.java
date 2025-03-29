package com.smartinvent.repositories;


import com.smartinvent.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TransactionRepository interface
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
