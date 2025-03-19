package com.smartinvent.repositories;

import com.smartinvent.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmployeeWorkId(Integer employeeWorkId);
    Optional<Employee> findByEmail(String email);  // Додаємо метод пошуку за email

}