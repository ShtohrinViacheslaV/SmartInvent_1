package com.smartinvent.model;

public class AuthRequest {
    private Integer employeeWorkId;
    private String password;

    public AuthRequest() {
    }

    public AuthRequest(Integer employeeWorkId, String password) {
        this.employeeWorkId = employeeWorkId;
        this.password = password;
    }

    public Integer getEmployeeWorkId() {
        return employeeWorkId;
    }

    public void setEmployeeWorkId(Integer employeeWorkId) {
        this.employeeWorkId = employeeWorkId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
