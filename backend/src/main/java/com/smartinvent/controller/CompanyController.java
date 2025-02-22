package com.smartinvent.controller;

import com.smartinvent.models.DatabaseConfig;
import com.smartinvent.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/config")
    public ResponseEntity<String> saveDatabaseConfig(@RequestBody DatabaseConfig config) {
        companyService.saveDatabaseConfig(config);
        return ResponseEntity.ok("Конфігурація бази даних збережена");
    }

    @GetMapping("/config")
    public ResponseEntity<DatabaseConfig> getDatabaseConfig() {
        return ResponseEntity.ok(companyService.getDatabaseConfig());
    }
}


//package com.smartinvent.controller;
//
//import com.smartinvent.models.DatabaseConfig;
//import com.smartinvent.service.CompanyService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/company")
//public class CompanyController {
//    private final CompanyService companyService;
//
//    @Autowired
//    public CompanyController(CompanyService companyService) {
//        this.companyService = companyService;
//    }
//
//    @PostMapping("/config")
//    public ResponseEntity<String> saveDatabaseConfig(@RequestBody DatabaseConfig config) {
//        companyService.saveDatabaseConfig(config);
//        return ResponseEntity.ok("Конфігурація бази даних збережена");
//    }
//
//    @GetMapping("/config")
//    public ResponseEntity<DatabaseConfig> getDatabaseConfig() {
//        return ResponseEntity.ok(companyService.getDatabaseConfig());
//    }
//}


//package com.smartinvent.controller;
//
//import com.smartinvent.models.Company;
//import com.smartinvent.service.CompanyService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/company")
//public class CompanyController {
//
//    private final CompanyService companyService;
//
//    @Autowired
//    public CompanyController(CompanyService companyService) {
//        this.companyService = companyService;
//    }
//
//
//    // Отримання всіх компаній
//    @GetMapping
//    public ResponseEntity<List<Company>> getAllCompanies() {
//        List<Company> companies = companyService.getAllCompanies();
//        return ResponseEntity.ok(companies);
//    }
//
//    // Додавання нової компанії
//    @PostMapping
//    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
//        Company createdCompany = companyService.createCompany(company);
//        return ResponseEntity.ok(createdCompany);
//    }
//
//    // Оновлення компанії
//    @PutMapping("/{id}")
//    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
//        Company updatedCompany = companyService.updateCompany(id, company);
//        return ResponseEntity.ok(updatedCompany);
//    }
//
//    // Видалення компанії
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
//        companyService.deleteCompany(id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//
//    @PostMapping("/register")
//    public ResponseEntity<String> registerCompany(@RequestBody CompanyRegistrationRequest request) {
//        companyService.registerCompany(request.getCompany(), request.getDbHost(), request.getDbUser(), request.getDbPassword());
//        return ResponseEntity.ok("Компанія зареєстрована!");
//    }
//
//    class CompanyRegistrationRequest {
//        private Company company;
//        private String dbHost;
//        private String dbUser;
//        private String dbPassword;
//
//        public Company getCompany() {
//            return company;
//        }
//
//        public void setCompany(Company company) {
//            this.company = company;
//        }
//
//        public String getDbHost() {
//            return dbHost;
//        }
//
//        public void setDbHost(String dbHost) {
//            this.dbHost = dbHost;
//        }
//
//        public String getDbUser() {
//            return dbUser;
//        }
//
//        public void setDbUser(String dbUser) {
//            this.dbUser = dbUser;
//        }
//
//        public String getDbPassword() {
//            return dbPassword;
//        }
//
//        public void setDbPassword(String dbPassword) {
//            this.dbPassword = dbPassword;
//        }
//    }
//
//
//
//
//}
