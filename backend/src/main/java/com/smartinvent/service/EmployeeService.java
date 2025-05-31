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
        if (admin == null) {
            throw new IllegalArgumentException("Admin cannot be null");
        }
        admin.setRole(RoleEnum.ADMIN);

        // Перевіряємо, чи пароль вже захешований або ні (якщо передають plain password, хешуємо)
        String rawPassword = admin.getPasswordHash();
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        admin.setPasswordHash(passwordEncoder.encode(rawPassword));

        // Зберігаємо співробітника в базі даних
        return employeeRepository.save(admin);
    }

    public boolean isEmployeeUnique(Long companyId, String email, String phone, String employeeWorkId, Long employeeIdToExclude) {
        if (companyId == null) {
            throw new IllegalArgumentException("Company ID cannot be null");
        }

        boolean emailExists;
        boolean phoneExists;
        boolean workIdExists;

        if (employeeIdToExclude == null) {
            // Для створення
            emailExists = email != null && employeeRepository.existsByCompany_CompanyIdAndEmail(companyId, email);
            phoneExists = phone != null && employeeRepository.existsByCompany_CompanyIdAndPhone(companyId, phone);
            workIdExists = employeeWorkId != null && employeeRepository.existsByCompany_CompanyIdAndEmployeeWorkId(companyId, employeeWorkId);
        } else {
            // Для оновлення - виключаємо самого себе
            emailExists = email != null && employeeRepository.existsByCompany_CompanyIdAndEmailAndEmployeeIdNot(companyId, email, employeeIdToExclude);
            phoneExists = phone != null && employeeRepository.existsByCompany_CompanyIdAndPhoneAndEmployeeIdNot(companyId, phone, employeeIdToExclude);
            workIdExists = employeeWorkId != null && employeeRepository.existsByCompany_CompanyIdAndEmployeeWorkIdAndEmployeeIdNot(companyId, employeeWorkId, employeeIdToExclude);
        }

        return !(emailExists || phoneExists || workIdExists);
    }



    public Employee createEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }

        if (!isEmployeeUnique(
                employee.getCompany().getCompanyId(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getEmployeeWorkId(),
                null)) {
            throw new RuntimeException("Employee with given email, phone or work ID already exists");
        }

        String password = generatePassword();
        employee.setPasswordHash(passwordEncoder.encode(password));

        // Призначаємо роль "USER"
        employee.setRole(RoleEnum.USER);


        // Відправляємо email
        emailService.sendEmail(
                employee.getEmail(),
                "Your login details for SmartInvent",
                "Good day, " + employee.getFirstName() + "!\n\n"
                        + "You are registered in the SmartInvent system as an employee of the company.\n\n"
                        + "Your login: " + employee.getEmployeeWorkId() + "\n"
                        + "Password: " + password + "\n\n"
                        + "We recommend changing your password after your first login.\n\n"
                        + "Best regards,\nThe SmartInvent Team"
        );


        // Зберігаємо співробітника
        return employeeRepository.save(employee);
    }


    // Отримання всіх співробітників
    public List<Employee> getAllEmployees(Long companyId) {
        if (companyId == null) {
            throw new IllegalArgumentException("Company ID cannot be null");
        }
        return employeeRepository.findByCompany_CompanyId(companyId);
    }

    // Отримання співробітника за id
    public Employee getEmployeeById(Long employeeId) {
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // Отримання співробітника за EmployeeWorkId
    public Employee getEmployeeByEmployeeWorkId(String employeeWorkId) {
        if (employeeWorkId == null || employeeWorkId.isEmpty()) {
            throw new IllegalArgumentException("Employee Work ID cannot be null or empty");
        }
        return employeeRepository.findByEmployeeWorkId(employeeWorkId)
                .orElseThrow(() -> new RuntimeException("Employee not found with Employee Work ID: " + employeeWorkId));
    }

    // Пошук співробітників за прізвищем
    public List<Employee> searchEmployeesByLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        return employeeRepository.findByLastNameContainingIgnoreCase(lastName);
    }


    // Пошук співробітників за запитом
    public List<Employee> searchEmployees(String query) {
        return employeeRepository.findByEmployeeWorkIdContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
    }


    // Редагування співробітника
    public Employee updateEmployee(Long employeeId, Employee updatedEmployee) {
        if (employeeId == null || updatedEmployee == null) {
            throw new IllegalArgumentException("Employee ID and updated employee cannot be null");
        }

        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));


        if (!isEmployeeUnique(
                updatedEmployee.getCompany().getCompanyId(),
                updatedEmployee.getEmail(),
                updatedEmployee.getPhone(),
                updatedEmployee.getEmployeeWorkId(),
                employeeId)) {
            throw new RuntimeException("Another employee with given email, phone or work ID already exists");
        }

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
        return UUID.randomUUID().toString();
    }

    public void deleteEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee with ID " + employeeId + " not found");
        }
        employeeRepository.deleteById(employeeId);
    }

    public void updatePassword(Long employeeId, String currentPassword, String newPassword) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!passwordEncoder.matches(currentPassword, employee.getPasswordHash())) {
            throw new RuntimeException("Current password is incorrect");
        }

        employee.setPasswordHash(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);
    }

    public void updateContactInfo(Long employeeId, String email, String phone) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Long companyId = employee.getCompany().getCompanyId();

        if (!isEmployeeUnique(companyId, email, phone, employee.getEmployeeWorkId(), employeeId)) {
            throw new RuntimeException("Email or phone already exists for another employee");
        }

        employee.setEmail(email);
        employee.setPhone(phone);
        employeeRepository.save(employee);
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
