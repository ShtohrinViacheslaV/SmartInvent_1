package com.smartinvent.controller;

import com.smartinvent.models.Employee;
import com.smartinvent.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping
    public ResponseEntity<?> registerAdmin(@RequestBody Employee admin) {
        if (employeeService.existsByEmployeeWorkId(admin.getEmployeeWorkId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Employee work ID already exists");
        }
        Employee savedAdmin = employeeService.registerAdmin(admin);
        return ResponseEntity.ok(savedAdmin);
    }

}
