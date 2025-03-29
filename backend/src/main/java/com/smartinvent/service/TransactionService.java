package com.smartinvent.service;

import com.smartinvent.models.Transaction;
import com.smartinvent.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TransactionService клас для роботи з Transaction об'єктами.
 */
@Service
public class TransactionService {

    /**
     * TransactionRepository об'єкт для роботи з Transaction об'єктами.
     *
     * @see com.smartinvent.repositories.TransactionRepository
     */
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Метод createTransaction для створення нового Transaction об'єкта.
     *
     * @param transaction Transaction об'єкт
     * @return створений Transaction об'єкт
     */
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Метод getAllTransactions для отримання всіх Transaction об'єктів.
     *
     * @return список Transaction об'єктів
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * getTransactionById метод для отримання Transaction об'єкта за його id.
     *
     * @param id id Transaction об'єкта
     * @return Transaction об'єкт
     */
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    /**
     * updateTransaction метод для оновлення інформації про Transaction об'єкт.
     *
     * @param id          id Transaction об'єкта
     * @param transaction Transaction об'єкт
     * @return оновлений Transaction об'єкт
     */
    public Transaction updateTransaction(Long id, Transaction transaction) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        existingTransaction.setType(transaction.getType());
        existingTransaction.setQuantity(transaction.getQuantity());
        existingTransaction.setDate(transaction.getDate());
        existingTransaction.setEmployee(transaction.getEmployee());
        existingTransaction.setStorage(transaction.getStorage());
        existingTransaction.setProduct(transaction.getProduct());

        return transactionRepository.save(existingTransaction);
    }

    /**
     * deleteTransaction метод для видалення Transaction об'єкта за його id.
     *
     * @param id id Transaction об'єкта
     */
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        transactionRepository.delete(transaction);
    }
}
