//package com.smartinvent.models;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "customer")
//public class Customer {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long customerId;
//
//    @ManyToOne
//    @JoinColumn(name = "company_id", nullable = false)
//    private Company company;
//
//    private String name;
//    private String contactInfo;
//    private String email;
//    private String phone;
//
//    // Конструктор без параметрів
//    public Customer() {}
//
//    // Конструктор для тестів
//    public Customer(String name, String contactInfo, String email, String phone) {
//        this.name = name;
//        this.contactInfo = contactInfo;
//        this.email = email;
//        this.phone = phone;
//    }
//
//
//    public Long getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(Long customerId) {
//        this.customerId = customerId;
//    }
//
//    public Company getCompany() {
//        return company;
//    }
//
//    public void setCompany(Company company) {
//        this.company = company;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getContactInfo() {
//        return contactInfo;
//    }
//
//    public void setContactInfo(String contactInfo) {
//        this.contactInfo = contactInfo;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//}
