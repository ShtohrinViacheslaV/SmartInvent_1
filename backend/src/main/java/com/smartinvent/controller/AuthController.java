package com.smartinvent.controller;

import com.smartinvent.dto.AuthRequest;
import com.smartinvent.dto.AuthResponse;
import com.smartinvent.dto.ForgotPasswordRequest;
import com.smartinvent.models.Employee;
import com.smartinvent.repositories.EmployeeRepository;
import com.smartinvent.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private EmailService emailService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Пошук співробітника по employeeWorkId
        Optional<Employee> employeeOpt = employeeRepository.findByEmployeeWorkId(request.getEmployeeWorkId());

        // Перевірка, чи знайдено співробітника і чи правильно введено пароль
        if (employeeOpt.isPresent() && passwordEncoder.matches(request.getPassword(), employeeOpt.get().getPasswordHash())) {
            Employee employee = employeeOpt.get();

            // Створюємо і повертаємо відповідь
            AuthResponse response = new AuthResponse(
                    employee.getEmployeeId(),
                    employee.getRole().name(), // Передаємо назву ролі
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getCompany().getCompanyId()
            );

            return ResponseEntity.ok(response);
        }

        // Якщо співробітника не знайдено або пароль неправильний
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("error", "Invalid work ID or password"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Optional<Employee> employeeOpt  = employeeRepository.findByEmployeeWorkIdAndPhoneAndEmail(
                request.getEmployeeWorkId(), request.getPhone(), request.getEmail()
        );

        if (employeeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Користувача не знайдено");
        }

        Employee employee = employeeOpt.get();
        String newPassword = generateSecurePassword(10);
        employee.setPasswordHash(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);

        emailService.sendEmail(
                employee.getEmail(),
                "Відновлення пароля",
                "Ваш новий пароль: " + newPassword
        );

        return ResponseEntity.ok("Новий пароль надіслано");

    }

    private String generateSecurePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#&$";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
