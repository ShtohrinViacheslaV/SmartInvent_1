//package com.smartinvent.models;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "data_change_log")
//public class DataChangeLog {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long changeId;
//
//    private String tableName;
//    private Long recordId;
//    private String fieldName;
//    private String oldValue;
//    private String newValue;
//    private LocalDateTime timestamp;
//
//    @ManyToOne
//    @JoinColumn(name = "employee_id", nullable = false)
//    private Employee employee;
//
//    @ManyToOne
//    @JoinColumn(name = "company_id", nullable = false)
//    private Company company;
//
//
//    public Long getChangeId() {
//        return changeId;
//    }
//
//    public void setChangeId(Long changeId) {
//        this.changeId = changeId;
//    }
//
//    public String getTableName() {
//        return tableName;
//    }
//
//    public void setTableName(String tableName) {
//        this.tableName = tableName;
//    }
//
//    public Long getRecordId() {
//        return recordId;
//    }
//
//    public void setRecordId(Long recordId) {
//        this.recordId = recordId;
//    }
//
//    public String getFieldName() {
//        return fieldName;
//    }
//
//    public void setFieldName(String fieldName) {
//        this.fieldName = fieldName;
//    }
//
//    public String getOldValue() {
//        return oldValue;
//    }
//
//    public void setOldValue(String oldValue) {
//        this.oldValue = oldValue;
//    }
//
//    public String getNewValue() {
//        return newValue;
//    }
//
//    public void setNewValue(String newValue) {
//        this.newValue = newValue;
//    }
//
//    public LocalDateTime getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(LocalDateTime timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public Employee getEmployee() {
//        return employee;
//    }
//
//    public void setEmployee(Employee employee) {
//        this.employee = employee;
//    }
//
//    public Company getCompany() {
//        return company;
//    }
//
//    public void setCompany(Company company) {
//        this.company = company;
//    }
//}
