package com.smartinvent.controller;

import com.smartinvent.dto.AuthRequest;
import com.smartinvent.dto.AuthResponse;
import com.smartinvent.models.Employee;
import com.smartinvent.repositories.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
