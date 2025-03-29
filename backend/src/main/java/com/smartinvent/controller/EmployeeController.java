package com.smartinvent.controller;

import com.smartinvent.models.Employee;
import com.smartinvent.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Клас-контроллер для обробки запитів, пов'язаних з працівниками
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    /**
     * Об'єкт сервісу для роботи з працівниками
     */
    private final EmployeeService employeeService;

    /**
     * Метод для реєстрації адміністратора
     *
     * @param admin - об'єкт адміністратора
     * @return - відповідь про успішність реєстрації
     */
    @PostMapping
    public ResponseEntity<Employee> registerAdmin(@RequestBody Employee admin) {
        if (employeeService.existsByEmployeeWorkId(admin.getEmployeeWorkId())) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(employeeService.registerAdmin(admin));
    }
}
