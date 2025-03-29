package com.smartinvent.service;

import com.smartinvent.models.Company;
import com.smartinvent.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CompanyService клас для роботи з Company об'єктами.
 */
@Service
@RequiredArgsConstructor
public class CompanyService {

    /**
     * CompanyRepository об'єкт для роботи з Company об'єктами.
     *
     * @see com.smartinvent.repositories.CompanyRepository
     */
    private final CompanyRepository companyRepository;

    /**
     * Метод для створення нового Company об'єкта.
     *
     * @param company Company об'єкт
     * @return створений Company об'єкт
     */
    @Transactional
    public Company registerCompany(Company company) {

//        company.setCreatedAt(LocalDateTime.now());
        return companyRepository.save(company);
    }

    /**
     * Метод getCompanyById для отримання Company об'єкта за його id.
     *
     * @param id id Company об'єкта
     * @return Company об'єкт
     */
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Компанію не знайдено"));
    }

    /**
     * Метод existsByName для перевірки наявності компанії за назвою.
     *
     * @param name назва компанії
     * @return true, якщо компанія з такою назвою існує, інакше - false
     */
    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }

    /**
     * Метод existsByPhone для перевірки наявності компанії за телефоном.
     *
     * @param phone телефон компанії
     * @return true, якщо компанія з таким телефоном існує, інакше - false
     */
    public boolean existsByPhone(String phone) {
        return companyRepository.existsByPhone(phone);
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
