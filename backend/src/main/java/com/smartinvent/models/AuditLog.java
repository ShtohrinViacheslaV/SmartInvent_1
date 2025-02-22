//package com.smartinvent.models;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "audit_log")
//public class AuditLog {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long auditId;
//
//    private String operationType;
//    private String tableName;
//    private Long recordId;
//    private String details;
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
//    public Long getAuditId() {
//        return auditId;
//    }
//
//    public void setAuditId(Long auditId) {
//        this.auditId = auditId;
//    }
//
//    public String getOperationType() {
//        return operationType;
//    }
//
//    public void setOperationType(String operationType) {
//        this.operationType = operationType;
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
//    public String getDetails() {
//        return details;
//    }
//
//    public void setDetails(String details) {
//        this.details = details;
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
