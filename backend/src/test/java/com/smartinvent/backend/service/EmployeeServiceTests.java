//package com.smartinvent.backend.service;
//
//import com.smartinvent.models.Employee;
//import com.smartinvent.repositories.EmployeeRepository;
//import com.smartinvent.service.EmployeeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class EmployeeServiceTests {
//
//    private EmployeeRepository employeeRepository;
//    private EmployeeService employeeService;
//
//    @BeforeEach
//    void setUp() {
//        employeeRepository = mock(EmployeeRepository.class); // Мок репозиторію
//        employeeService = new EmployeeService(employeeRepository); // Ін'єкція через конструктор
//    }
//
//    @Test
//    void testCreateEmployee() {
//        // Мок об'єкта
//        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "Manager");
//
//        when(employeeRepository.save(employee)).thenReturn(employee);
//
//        // Виклик методу
//        Employee result = employeeService.createEmployee(employee);
//
//        // Перевірки
//        assertNotNull(result);
//        assertEquals("John", result.getFirstName());
//        assertEquals("Doe", result.getLastName());
//        verify(employeeRepository, times(1)).save(employee);
//    }
//
//    @Test
//    void testUpdateEmployee_ValidId() {
//        Long employeeId = 1L;
//        Employee existingEmployee = new Employee("John", "Doe", "john.doe@example.com", "Manager");
//        Employee updatedEmployee = new Employee("Jane", "Doe", "jane.doe@example.com", "Supervisor");
//
//        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
//        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);
//
//        // Виклик методу
//        Employee result = employeeService.updateEmployee(employeeId, updatedEmployee);
//
//        // Перевірки
//        assertNotNull(result);
//        assertEquals("Jane", result.getFirstName());
//        assertEquals("Doe", result.getLastName());
//        verify(employeeRepository, times(1)).findById(employeeId);
//        verify(employeeRepository, times(1)).save(existingEmployee);
//    }
//
//    @Test
//    void testDeleteEmployee_ValidId() {
//        Long employeeId = 1L;
//        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "Manager");
//
//        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
//        doNothing().when(employeeRepository).delete(employee);
//
//        // Виклик методу
//        assertDoesNotThrow(() -> employeeService.deleteEmployee(employeeId));
//
//        // Перевірки
//        verify(employeeRepository, times(1)).findById(employeeId);
//        verify(employeeRepository, times(1)).delete(employee);
//    }
//}