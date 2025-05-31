package com.smartinvent.dto;

public class ForgotPasswordRequest {
    private String employeeWorkId;
    private String phone;
    private String email;

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
}
