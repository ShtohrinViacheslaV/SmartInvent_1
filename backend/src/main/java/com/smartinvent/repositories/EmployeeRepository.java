package com.smartinvent.repositories;

import com.smartinvent.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByCompany_CompanyIdAndEmail(Long companyId, String email);
    boolean existsByCompany_CompanyIdAndPhone(Long companyId, String phone);
    boolean existsByCompany_CompanyIdAndEmployeeWorkId(Long companyId, String employeeWorkId);

    boolean existsByCompany_CompanyIdAndEmailAndEmployeeIdNot(Long companyId, String email, Long employeeId);
    boolean existsByCompany_CompanyIdAndPhoneAndEmployeeIdNot(Long companyId, String phone, Long employeeId);
    boolean existsByCompany_CompanyIdAndEmployeeWorkIdAndEmployeeIdNot(Long companyId, String employeeWorkId, Long employeeId);

    Optional<Employee> findByEmployeeWorkIdAndPhoneAndEmail(String EmployeeWorkId, String phone, String email);


    List<Employee> findByEmployeeWorkIdContainingIgnoreCaseOrLastNameContainingIgnoreCase(String employeeWorkId, String lastName);



    Optional<Employee> findByEmployeeWorkId(String employeeWorkId);
    List<Employee> findByCompany_CompanyId(Long companyId);
    Optional<Employee> findByEmail(String email);
    List<Employee> findByLastNameContainingIgnoreCase(String lastName);


}