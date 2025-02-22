package com.smartinvent.service;

import com.smartinvent.config.ConfigService;
import com.smartinvent.models.Company;
import com.smartinvent.models.DatabaseConfig;
import com.smartinvent.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ConfigService configService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, ConfigService configService) {
        this.companyRepository = companyRepository;
        this.configService = configService;
    }

    @Transactional
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    @Transactional
    public Company updateCompany(Long id, Company company) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        existingCompany.setName(company.getName());
        existingCompany.setAddress(company.getAddress());
        existingCompany.setPhone(company.getPhone());
        existingCompany.setEmail(company.getEmail());
        existingCompany.setCreatedAt(company.getCreatedAt());
        return companyRepository.save(existingCompany);
    }

    @Transactional
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        companyRepository.delete(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    // Збереження конфігурації БД
    public void saveDatabaseConfig(DatabaseConfig config) {
        configService.saveConfig(config);
    }

    // Отримання конфігурації БД
    public DatabaseConfig getDatabaseConfig() {
        return configService.getDatabaseConfig();
    }
}


//package com.smartinvent.service;
//
//import com.smartinvent.config.ConfigService;
//import com.smartinvent.models.Company;
//import com.smartinvent.repositories.CompanyRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//import java.util.List;
//
//
//@Service
//public class CompanyService {
//    private final CompanyRepository companyRepository;
//    private final ConfigService configService;
//
//    @Autowired
//    public CompanyService(CompanyRepository companyRepository, ConfigService configService) {
//        this.companyRepository = companyRepository;
//        this.configService = configService;
//    }
//
//    @Transactional
//    public Company createCompany(Company company) {
//        return companyRepository.save(company);
//    }
//
//
//    @Transactional
//    public Company updateCompany(Long id, Company company) {
//        Company existingCompany = companyRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
//        existingCompany.setName(company.getName());
//        existingCompany.setAddress(company.getAddress());
//        existingCompany.setPhone(company.getPhone());
//        existingCompany.setEmail(company.getEmail());
//        existingCompany.setCreatedAt(company.getCreatedAt());
//        return companyRepository.save(existingCompany);
//    }
//
//    @Transactional
//    public void deleteCompany(Long id) {
//        Company company = companyRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
//        companyRepository.delete(company);
//    }
//
//    public List<Company> getAllCompanies() {
//        return companyRepository.findAll();
//    }
//
//    public Company getCompanyById(Long id) {
//        return companyRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
//    }
//
////    @Transactional
////    public Company registerCompany(Company company, String dbHost, String dbUser, String dbPassword) {
////        if (configService.isConfigFileExists()) {
////            throw new RuntimeException("Database configuration already exists! Registration is allowed only once.");
////        }
////
////        Company savedCompany = companyRepository.save(company);
////        String dbUrl = "jdbc:postgresql://" + dbHost + ":5432/" + company.getName().toLowerCase() + "_db";
////
////        try {
////
////            configService.saveConfig(dbUrl, dbUser, dbPassword);
////        } catch (IOException e) {
////            throw new RuntimeException("Failed to save database configuration", e);
////        }
////
////        return savedCompany;
////    }
//}
