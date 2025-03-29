package com.smartinvent.controller;

import com.smartinvent.dto.AuthRequest;
import com.smartinvent.dto.AuthResponse;
import com.smartinvent.models.Employee;
import com.smartinvent.repositories.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Клас-контроллер для обробки запитів, пов'язаних з авторизацією
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Репозиторій для роботи з працівниками
     */
    private final EmployeeRepository employeeRepository;

    /**
     * Об'єкт для шифрування паролів
     */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Конструктор класу
     *
     * @param employeeRepository - репозиторій для роботи з працівниками
     * @param passwordEncoder    - об'єкт для шифрування паролів
     */
    public AuthController(EmployeeRepository employeeRepository, BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Метод для авторизації користувача
     *
     * @param request - об'єкт запиту авторизації
     * @return - відповідь з даними про користувача
     */
    @PostMapping("/login")
//    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmail(request.getEmail());

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            if (passwordEncoder.matches(request.getPassword(), employee.getPasswordHash())) {
                return ResponseEntity.ok(new AuthResponse(
                        employee.getEmployeeId(),
                        employee.getRole(),
                        employee.getFirstName(),
                        employee.getLastName()
                ));
            }
        }
        return ResponseEntity.status(401).body("Invalid email or password");
    }
}
