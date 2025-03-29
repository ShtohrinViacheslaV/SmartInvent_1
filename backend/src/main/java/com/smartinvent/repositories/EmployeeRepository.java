package com.smartinvent.repositories;

import com.smartinvent.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * EmployeeRepository interface
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * existsByEmployeeWorkId метод перевіряє чи існує співробітник з вказаним employeeWorkId
     *
     * @param employeeWorkId - ідентифікатор співробітника
     * @return true якщо співробітник існує, false якщо ні
     */
    boolean existsByEmployeeWorkId(Integer employeeWorkId);

    /**
     * existsByEmail метод перевіряє чи існує співробітник з вказаним email
     *
     * @param email - email співробітника
     * @return true якщо співробітник існує, false якщо ні
     */
    Optional<Employee> findByEmail(String email);  // Додаємо метод пошуку за email
}