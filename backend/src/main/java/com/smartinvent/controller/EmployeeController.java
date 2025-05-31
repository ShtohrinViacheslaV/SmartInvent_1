package com.smartinvent.controller;

import com.smartinvent.dto.UpdateContactRequest;
import com.smartinvent.dto.UpdatePasswordRequest;
import com.smartinvent.models.Employee;
import com.smartinvent.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // Отримання списку всіх співробітників
    @GetMapping("/all")
    public List<Employee> getAllEmployees(@RequestParam Long companyId) {
        return employeeService.getAllEmployees(companyId);
    }

    // Отримання співробітника за id
    @GetMapping("/{employeeId}")
    public Employee getEmployeeById(@PathVariable Long employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    // Створення нового співробітника
    @PostMapping("/create")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    // Оновлення даних співробітника
    @PutMapping("/update/{employeeId}")
    public Employee updateEmployee(@PathVariable Long employeeId, @RequestBody Employee employee) {
        return employeeService.updateEmployee(employeeId, employee);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam String query) {
        List<Employee> employees = employeeService.searchEmployees(query);
        if (employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(employees);
    }



    @PostMapping
    public ResponseEntity<?> registerAdmin(@RequestBody Employee admin) {
        if (!employeeService.isEmployeeUnique(
                admin.getCompany().getCompanyId(),
                admin.getEmail(), admin.getPhone(),
                admin.getEmployeeWorkId(),
                null)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Employee with given email, phone or work ID already exists");
        }


        Employee savedAdmin = employeeService.registerAdmin(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
    }

    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    @GetMapping("/byWorkId/{employeeWorkId}")
    public Employee getEmployeeByEmployeeWorkId(@PathVariable String employeeWorkId) {
        return employeeService.getEmployeeByEmployeeWorkId(employeeWorkId);
    }

    @GetMapping("/search/{lastName}")
    public List<Employee> searchEmployeesByLastName(@PathVariable String lastName) {
        return employeeService.searchEmployeesByLastName(lastName);
    }

    @GetMapping("/check-unique")
    public boolean isEmployeeUnique(@RequestParam Long companyId,
                                    @RequestParam String email,
                                    @RequestParam String phone,
                                    @RequestParam String employeeWorkId) {
        return employeeService.isEmployeeUnique(companyId, email, phone, employeeWorkId, null);
    }

    @GetMapping("/check-unique-exclude")
    public boolean isEmployeeUniqueExclude(@RequestParam Long companyId,
                                           @RequestParam String email,
                                           @RequestParam String phone,
                                           @RequestParam String employeeWorkId,
                                           @RequestParam Long employeeIdToExclude) {
        return employeeService.isEmployeeUnique(companyId, email, phone, employeeWorkId, employeeIdToExclude);
    }

    @PutMapping("/update-password/{employeeId}")
    public ResponseEntity<String> updatePassword(@PathVariable Long employeeId,
                                                 @RequestBody UpdatePasswordRequest request) {
        try {
            employeeService.updatePassword(employeeId, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok("Password updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-contact/{employeeId}")
    public ResponseEntity<String> updateContactInfo(@PathVariable Long employeeId,
                                                    @RequestBody UpdateContactRequest request) {
        try {
            employeeService.updateContactInfo(employeeId, request.getEmail(), request.getPhone());
            return ResponseEntity.ok("Contact information updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }



}
