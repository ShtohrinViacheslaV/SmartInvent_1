package com.smartinvent.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String phone;

    @Column(nullable = false, length = 50)
    private String employeeWorkId;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public Employee() {
    }

    public Employee(Long employeeId, Company company, String firstName, String lastName, String email, String phone, String employeeWorkId, String passwordHash, RoleEnum role) {
        this.employeeId = employeeId;
        this.company = company;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.employeeWorkId = employeeWorkId;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmployeeWorkId() {
        return employeeWorkId;
    }

    public void setEmployeeWorkId(String employeeWorkId) {
        this.employeeWorkId = employeeWorkId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}


//
//@Entity
//@Table(name = "employee")
//public class Employee {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long employeeId;
//
//    private String firstName;
//    private String lastName;
//    private String email;
//    private String employeeWorkId;
//    private String passwordHash;
//    private String role;
//
//
//    // Конструктор без параметрів
//    public Employee() {}
//
//    // Конструктор для тестів
//    public Employee(String firstName, String lastName, String email, String role) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.employeeWorkId = employeeWorkId;
//        this.passwordHash = passwordHash;
//        this.role = role;
//    }
//
//
//    public Long getEmployeeId() {
//        return employeeId;
//    }
//
//    public void setEmployeeId(Long employeeId) {
//        this.employeeId = employeeId;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
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
//    public String getEmployeeWorkId() {
//        return employeeWorkId;
//    }
//
//    public void setEmployeeWorkId(String employeeWorkId) {
//        this.employeeWorkId = employeeWorkId;
//    }
//
//    public String getPasswordHash() {
//        return passwordHash;
//    }
//
//    public void setPasswordHash(String passwordHash) {
//        this.passwordHash = passwordHash;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//}
