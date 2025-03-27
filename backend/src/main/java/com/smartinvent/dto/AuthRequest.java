package com.smartinvent.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String employeeWorkId;
    private String password;

    public String getEmployeeWorkId() {
        return employeeWorkId;
    }

    public void setEmployeeWorkId(String employeeWorkId) {
        this.employeeWorkId = employeeWorkId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
