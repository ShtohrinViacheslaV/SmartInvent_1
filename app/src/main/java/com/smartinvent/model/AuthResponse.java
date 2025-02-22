package com.smartinvent.model;



public class AuthResponse {
    private Long employeeId;
    private String role;
    private String firstName;
    private String lastName;


    public AuthResponse() {
    }

    public AuthResponse(String lastName, String firstName, String role, Long employeeId) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
        this.employeeId = employeeId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}