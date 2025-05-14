package com.smartinvent.service;

import com.smartinvent.models.Employee;
import com.smartinvent.models.RoleEnum;
import com.smartinvent.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Transactional
    public Employee registerAdmin(Employee admin) {
        // Отримуємо роль "ADMIN" як enum
        RoleEnum adminRole = RoleEnum.ADMIN;

        // Встановлюємо роль для адміна
        admin.setRole(adminRole);

        // Хешуємо пароль
        admin.setPasswordHash(passwordEncoder.encode(admin.getPasswordHash()));

        // Зберігаємо співробітника в базі даних
        return employeeRepository.save(admin);
    }



    public boolean existsByEmployeeWorkId(String employeeWorkId) {
        return employeeRepository.existsByEmployeeWorkId(employeeWorkId);
    }

    // Створення нового співробітника
    public Employee createEmployee(Employee employee) {
        // Генеруємо пароль
        String password = generatePassword();
        employee.setPasswordHash(passwordEncoder.encode(password));

        // Призначаємо роль "USER"
        RoleEnum role = RoleEnum.USER;
        employee.setRole(role);

        // Відправляємо email
        emailService.sendEmail(employee.getEmail(), "Your Login Credentials",
                "Login: " + employee.getEmail() + "\nPassword: " + password);

        // Зберігаємо співробітника
        return employeeRepository.save(employee);
    }


    // Отримання всіх співробітників
    public List<Employee> getAllEmployees(Long companyId) {
        return employeeRepository.findByCompanyId(companyId);
    }

    // Отримання співробітника за id
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // Редагування співробітника
    public Employee updateEmployee(Long employeeId, Employee updatedEmployee) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Оновлюємо поля
        existingEmployee.setFirstName(updatedEmployee.getFirstName());
        existingEmployee.setLastName(updatedEmployee.getLastName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setPhone(updatedEmployee.getPhone());

        if (updatedEmployee.getRole() != null) {
            // Заміна на використання Enum для ролі
            RoleEnum roleEnum = updatedEmployee.getRole(); // Це тепер Enum
            existingEmployee.setRole(roleEnum); // Прив'язка Enum
        }

        return employeeRepository.save(existingEmployee); // Збереження оновленого співробітника
    }


    // Генерація пароля
    private String generatePassword() {
        // Можна реалізувати більш складну генерацію пароля або використовувати бібліотеки для цього
        return UUID.randomUUID().toString();
    }

}


//@Service
//public class EmployeeService {
//
//    private final EmployeeRepository employeeRepository;
//
//    @Autowired
//    public EmployeeService(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }
//
//
//    public Employee createEmployee(Employee employee) {
//        return employeeRepository.save(employee);
//    }
//
//    public Employee updateEmployee(Long id, Employee employee) {
//        Employee existingEmployee = employeeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
//        existingEmployee.setFirstName(employee.getFirstName());
//        existingEmployee.setLastName(employee.getLastName());
//        existingEmployee.setEmail(employee.getEmail());
//        existingEmployee.setEmployeeWorkId(employee.getEmployeeWorkId());
//        existingEmployee.setPasswordHash(employee.getPasswordHash());
//        existingEmployee.setRole(employee.getRole());
//        return employeeRepository.save(existingEmployee);
//    }
//
//    public void deleteEmployee(Long id) {
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
//        employeeRepository.delete(employee);
//    }
//
//    public List<Employee> getAllEmployees() {
//        return employeeRepository.findAll();
//    }
//
//    public Employee getEmployeeById(Long id) {
//        return employeeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
//    }
//}
