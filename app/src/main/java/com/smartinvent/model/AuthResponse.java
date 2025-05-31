package com.smartinvent.model;



public class AuthResponse {
    private Long employeeId;
    private RoleEnum role;
    private String firstName;
    private String lastName;
    private Long companyId;


    public AuthResponse() {
    }

    public AuthResponse(String lastName, String firstName, RoleEnum role, Long employeeId, Long companyId) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
        this.employeeId = employeeId;
        this.companyId = companyId;
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

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}