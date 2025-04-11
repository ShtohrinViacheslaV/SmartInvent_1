package com.smartinvent.controller;

import com.smartinvent.dto.AuthRequest;
import com.smartinvent.dto.AuthResponse;
import com.smartinvent.models.Employee;
import com.smartinvent.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(EmployeeRepository employeeRepository, BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        log.info("Login attempt with Work ID: {}", request.getEmployeeWorkId());

        try {
            Optional<Employee> employeeOpt = employeeRepository.findByEmployeeWorkId(request.getEmployeeWorkId());

            if (employeeOpt.isPresent()) {
                Employee employee = employeeOpt.get();

                if (passwordEncoder.matches(request.getPassword(), employee.getPasswordHash())) {
                    log.info("Login successful for Work ID: {}", request.getEmployeeWorkId());

                    return ResponseEntity.ok(new AuthResponse(
                            employee.getEmployeeId(),
                            employee.getRole(),
                            employee.getFirstName(),
                            employee.getLastName()
                    ));
                } else {
                    log.warn("Incorrect password for Work ID: {}", request.getEmployeeWorkId());
                }
            } else {
                log.warn("No employee found with Work ID: {}", request.getEmployeeWorkId());
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid work ID or password"));

        } catch (Exception e) {
            log.error("Unexpected error during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Server error during login"));
        }
    }
}