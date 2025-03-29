package com.smartinvent.service;

import com.smartinvent.models.Employee;
import com.smartinvent.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * EmployeeService клас для роботи з Employee об'єктами.
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    /**
     * EmployeeRepository об'єкт для роботи з Employee об'єктами.
     *
     * @see com.smartinvent.repositories.EmployeeRepository
     */
    private final EmployeeRepository employeeRepository;

    /**
     * PasswordEncoder об'єкт для шифрування паролів.
     *
     * @see org.springframework.security.crypto.password.PasswordEncoder
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Метод registerAdmin для реєстрації адміністратора.
     *
     * @param admin Employee об'єкт
     * @return Employee об'єкт
     */
    @Transactional
    public Employee registerAdmin(Employee admin) {
        admin.setRole("ADMIN");
        admin.setPasswordHash(passwordEncoder.encode(admin.getPasswordHash()));
        return employeeRepository.save(admin);
    }

    /**
     * existsByEmployeeWorkId метод для перевірки наявності працівника за його id.
     *
     * @param employeeWorkId id працівника
     * @return true, якщо працівник з таким id існує, інакше - false
     */
    public boolean existsByEmployeeWorkId(Integer employeeWorkId) {
        return employeeRepository.existsByEmployeeWorkId(employeeWorkId);
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
