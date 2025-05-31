package com.smartinvent.service;

import com.smartinvent.model.Employee;
import com.smartinvent.model.UpdateContactRequest;
import com.smartinvent.model.UpdatePasswordRequest;
import com.smartinvent.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;

import java.util.List;

public class EmployeeService {


    private final EmployeeApi employeeApi;

    public EmployeeService() {
        employeeApi = ApiClient.getClient().create(EmployeeApi.class);
    }

    public void getAllEmployees(Long companyId, Callback<List<Employee>> callback) {
        Call<List<Employee>> call = employeeApi.getAllEmployees(companyId);
        call.enqueue(callback);
    }

    public void getEmployeeById(Long employeeId, Callback<Employee> callback) {
        Call<Employee> call = employeeApi.getEmployeeById(employeeId);
        call.enqueue(callback);
    }

    public void createEmployee(Employee employee, Callback<Employee> callback) {
        Call<Employee> call = employeeApi.createEmployee(employee);
        call.enqueue(callback);
    }

    public void updateEmployee(Long id, Employee employee, Callback<Employee> callback) {
        Call<Employee> call = employeeApi.updateEmployee(id, employee);
        call.enqueue(callback);
    }

    public void deleteEmployee(Long id, Callback<Void> callback) {
        Call<Void> call = employeeApi.deleteEmployee(id);
        call.enqueue(callback);
    }

    public void getEmployeeByEmployeeWorkId(String employeeWorkId, Callback<Employee> callback) {
        Call<Employee> call = employeeApi.getEmployeeByEmployeeWorkId(employeeWorkId);
        call.enqueue(callback);
    }

    public void searchEmployeesByLastName(String lastName, Callback<List<Employee>> callback) {
        Call<List<Employee>> call = employeeApi.searchEmployeesByLastName(lastName);
        call.enqueue(callback);
    }


    public void isUniqueEmployee(Long companyId, String email, String phone, String employeeWorkId, Callback<Boolean> callback) {
        Call<Boolean> call = employeeApi.isEmployeeUnique(companyId, email, phone, employeeWorkId);
        call.enqueue(callback);
    }

    public void isUniqueEmployeeExclude(Long companyId, String email, String phone, String employeeWorkId, Long employeeIdToExclude, Callback<Boolean> callback) {
        Call<Boolean> call = employeeApi.isEmployeeUniqueExclude(companyId, email, phone, employeeWorkId, employeeIdToExclude);
        call.enqueue(callback);
    }

    public void updatePassword(Long employeeId, String currentPassword, String newPassword, Callback<String> callback) {
        UpdatePasswordRequest request = new UpdatePasswordRequest(currentPassword, newPassword);
        Call<String> call = employeeApi.updatePassword(employeeId, request);
        call.enqueue(callback);
    }

    public void updateContactInfo(Long employeeId, String email, String phone, Callback<String> callback) {
        UpdateContactRequest request = new UpdateContactRequest(email, phone);
        Call<String> call = employeeApi.updateContactInfo(employeeId, request);
        call.enqueue(callback);
    }




}
