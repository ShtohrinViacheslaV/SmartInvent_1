package com.smartinvent.model;

import java.time.LocalDateTime;


public class Transaction {

    private Long transactionId;
    private String type;
    private LocalDateTime date;
    private int quantity;
    private Employee employee;
    private Storage storage;
    private Product product;

    public Transaction(Long productId, String action, long l) {
    }

    public Transaction(Long transactionId, String type, LocalDateTime date, int quantity, Employee employee, Storage storage, Product product) {
        this.transactionId = transactionId;
        this.type = type;
        this.date = date;
        this.quantity = quantity;
        this.employee = employee;
        this.storage = storage;
        this.product = product;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

