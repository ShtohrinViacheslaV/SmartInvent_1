package com.smartinvent.repositories;

import com.smartinvent.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmployeeWorkId(String employeeWorkId);
    Optional<Employee> findByEmployeeWorkId(String employeeWorkId);
    List<Employee> findByCompanyId(Long companyId);
    Optional<Employee> findByEmail(String email);
}