package com.smartinvent.repositories;


import com.smartinvent.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByName(String name);
    boolean existsByPhone(String phone);
}