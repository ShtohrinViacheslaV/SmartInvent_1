package com.smartinvent.service;

import com.smartinvent.models.Company;
import com.smartinvent.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;


    @Transactional
    public Company registerCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Компанію не знайдено"));
    }

    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }

    public boolean existsByPhone(String phone) {
        return companyRepository.existsByPhone(phone);
    }
}
