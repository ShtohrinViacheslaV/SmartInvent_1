package com.smartinvent.controller;

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


    @PostMapping
    public ResponseEntity<?> registerAdmin(@RequestBody Employee admin) {
        // Перевірка наявності співробітника з таким employeeWorkId
        if (employeeService.existsByEmployeeWorkId(admin.getEmployeeWorkId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Employee work ID already exists");
        }

        // Реєстрація нового адміністратора
        Employee savedAdmin = employeeService.registerAdmin(admin);

        // Повертаємо успішний результат з інформацією про зареєстрованого адміністратора
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
    }

}
