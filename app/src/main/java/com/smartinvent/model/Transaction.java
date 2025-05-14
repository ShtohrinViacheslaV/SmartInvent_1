package com.smartinvent.model;


public class Transaction {
    private Long transactionId;
    private TransactionTypeEnum type;
    private Product product;
    private Employee employee;
    private int quantity;
    private String transactionDate;


    public Transaction() {
    }

    public Transaction(Long transactionId, TransactionTypeEnum type, Product product,
                       Employee employee, int quantity, String transactionDate) {
        this.transactionId = transactionId;
        this.type = type;
        this.product = product;
        this.employee = employee;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionTypeEnum getType() {
        return type;
    }

    public void setType(TransactionTypeEnum type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}

