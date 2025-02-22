//package com.smartinvent.service;
//
//import com.smartinvent.models.Customer;
//import com.smartinvent.repositories.CustomerRepository;
//import com.smartinvent.repositories.EmployeeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CustomerService {
//
//    private final CustomerRepository customerRepository;
//
//    @Autowired
//    public CustomerService(CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }
//
//
//    public Customer createCustomer(Customer customer) {
//        return customerRepository.save(customer);
//    }
//
//    public Customer updateCustomer(Long id, Customer customer) {
//        Customer existingCustomer = customerRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
//        existingCustomer.setName(customer.getName());
//        existingCustomer.setContactInfo(customer.getContactInfo());
//        existingCustomer.setEmail(customer.getEmail());
//        existingCustomer.setPhone(customer.getPhone());
//
//        return customerRepository.save(existingCustomer);
//    }
//
//    public void deleteCustomer(Long id) {
//        Customer customer = customerRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
//        customerRepository.delete(customer);
//    }
//
//    public List<Customer> getAllCustomers() {
//        return customerRepository.findAll();
//    }
//
//    public Customer getCustomerById(Long id) {
//        return customerRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
//    }
//}
