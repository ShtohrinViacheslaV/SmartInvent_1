package com.smartinvent.controller;

import com.smartinvent.models.Employee;
import com.smartinvent.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping
    public ResponseEntity<Employee> registerAdmin(@RequestBody Employee admin) {
        if (employeeService.existsByEmployeeWorkId(admin.getEmployeeWorkId())) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(employeeService.registerAdmin(admin));
    }
}
