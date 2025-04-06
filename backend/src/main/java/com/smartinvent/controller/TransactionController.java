package com.smartinvent.controller;

import com.smartinvent.models.Transaction;
import com.smartinvent.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Отримання всіх транзакцій
    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        final List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    // Додавання нової транзакції
    @PostMapping("/create")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        final Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.ok(createdTransaction);
    }

    // Оновлення транзакції
    @PostMapping("/update/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        final Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Видалення транзакції
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
