package com.smartinvent.controller;

import com.smartinvent.models.Transaction;
import com.smartinvent.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Клас-контроллер для обробки запитів, пов'язаних з транзакціями
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    /**
     * Об'єкт сервісу для роботи з транзакціями
     */
    @Autowired
    private TransactionService transactionService;

    /**
     * Метод для отримання всіх транзакцій
     *
     * @return - список транзакцій
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * Метод createTransaction для створення нової транзакції
     *
     * @param transaction - об'єкт транзакції
     * @return - створена транзакція
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.ok(createdTransaction);
    }

    /**
     * Метод updateTransaction для оновлення інформації про транзакцію
     *
     * @param id          - ідентифікатор транзакції
     * @param transaction - об'єкт транзакції
     * @return - оновлена транзакція
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * Метод deleteTransaction для видалення транзакції
     *
     * @param id - ідентифікатор транзакції
     * @return - видалена транзакція з бази даних
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
