package com.smartinvent.repositories;


import com.smartinvent.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
