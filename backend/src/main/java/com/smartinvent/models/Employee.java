package com.smartinvent.models;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String firstName;
    private String lastName;
    private String email;
    private Integer employeeWorkId;
    private String passwordHash;
    private String role;


    // Конструктор без параметрів
    public Employee() {}

    // Конструктор для тестів
    public Employee(String firstName, String lastName, String email, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEmployeeWorkId() {
        return employeeWorkId;
    }

    public void setEmployeeWorkId(Integer employeeWorkId) {
        this.employeeWorkId = employeeWorkId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
