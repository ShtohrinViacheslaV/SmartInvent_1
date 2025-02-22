//package com.smartinvent.controller;
//
//import com.smartinvent.models.Customer;
//import com.smartinvent.service.CustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/customers")
//public class CustomerController {
//
//    @Autowired
//    private CustomerService customerService;
//
//    // Отримання всіх клієнтів
//    @GetMapping
//    public ResponseEntity<List<Customer>> getAllCustomers() {
//        List<Customer> customers = customerService.getAllCustomers();
//        return ResponseEntity.ok(customers);
//    }
//
//    // Додавання нового клієнта
//    @PostMapping
//    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
//        Customer createdCustomer = customerService.createCustomer(customer);
//        return ResponseEntity.ok(createdCustomer);
//    }
//
//    // Оновлення клієнта
//    @PutMapping("/{id}")
//    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
//        Customer updatedCustomer = customerService.updateCustomer(id, customer);
//        return ResponseEntity.ok(updatedCustomer);
//    }
//
//    // Видалення клієнта
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
//        customerService.deleteCustomer(id);
//        return ResponseEntity.noContent().build();
//    }
//}
