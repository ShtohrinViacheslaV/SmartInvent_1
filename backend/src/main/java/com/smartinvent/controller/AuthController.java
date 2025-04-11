package com.smartinvent.controller;

import com.smartinvent.dto.AuthRequest;
import com.smartinvent.dto.AuthResponse;
import com.smartinvent.dto.ErrorResponse;
import com.smartinvent.models.Employee;
import com.smartinvent.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private MessageSource messageSource;

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(EmployeeRepository employeeRepository, BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String sessionId = UUID.randomUUID().toString();
        String userId = request.getEmployeeWorkId() != null ? request.getEmployeeWorkId() : "unknown";

        // Додаємо контекст до MDC
        MDC.put("sessionId", sessionId);
        MDC.put("userId", userId);

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
                    return handleLoginFailure("Incorrect password", request.getEmployeeWorkId());
                }
            } else {
                log.warn("No employee found with Work ID: {}", request.getEmployeeWorkId());
                return handleLoginFailure("Employee not found", request.getEmployeeWorkId());
            }

        } catch (Exception e) {
            log.error("Unexpected error during login", e);
            return handleLoginFailure("Server error during login", null);
        } finally {
            MDC.clear();
        }
    }

    private ResponseEntity<ErrorResponse> handleLoginFailure(String messageKey, String workId) {
        String errorId = UUID.randomUUID().toString();
        String localizedMessage = messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = new ErrorResponse(
                errorId,
                localizedMessage,
                LocalDateTime.now(),
                workId != null ? "Login attempt with Work ID: " + workId : "Login attempt",
                Collections.emptyMap()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
