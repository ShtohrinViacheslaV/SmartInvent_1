package com.smartinvent.service;

import com.smartinvent.models.Transaction;
import com.smartinvent.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }
        return transactionRepository.save(transaction);
    }


    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    public Transaction updateTransaction(Long id, Transaction transaction) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        existingTransaction.setType(transaction.getType());
        existingTransaction.setQuantity(transaction.getQuantity());
        existingTransaction.setDate(transaction.getDate());
        existingTransaction.setEmployee(transaction.getEmployee());
        existingTransaction.setProduct(transaction.getProduct());


        return transactionRepository.save(existingTransaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        transactionRepository.delete(transaction);
    }

    public List<Transaction> getTransactionsByEmployeeId(Long employeeId) {
        return transactionRepository.findByEmployee_EmployeeId(employeeId);
    }

}
