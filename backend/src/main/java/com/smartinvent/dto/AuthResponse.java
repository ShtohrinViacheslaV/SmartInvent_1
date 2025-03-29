package com.smartinvent.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Клас-запит для авторизації користувача
 */
@Getter
@Setter
public class AuthResponse {

    /**
     * Поле для зберігання ідентифікатора працівника
     */
    private Long employeeId;

    /**
     * Поле для зберігання ролі працівника
     */
    private String role;

    /**
     * Поле для зберігання імені працівника
     */
    private String firstName;

    /**
     * Поле для зберігання прізвища працівника
     */
    private String lastName;

    /**
     * Конструктор класу AuthResponse
     *
     * @param employeeId - ідентифікатор працівника
     * @param role       - роль працівника
     * @param firstName  - ім'я працівника
     * @param lastName   - прізвище працівника
     */
    public AuthResponse(Long employeeId, String role, String firstName, String lastName) {
        this.employeeId = employeeId;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * getEmployeeId - метод для отримання ідентифікатора працівника
     *
     * @return - ідентифікатор працівника
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * setEmployeeId - метод для встановлення ідентифікатора працівника
     *
     * @param employeeId - ідентифікатор працівника
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * getRole - метод для отримання ролі працівника
     *
     * @return - роль працівника
     */
    public String getRole() {
        return role;
    }

    /**
     * setRole - метод для встановлення ролі працівника
     *
     * @param role - роль працівника
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * getFirstName - метод для отримання імені працівника
     *
     * @return - ім'я працівника
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * setFirstName - метод для встановлення імені працівника
     *
     * @param firstName - ім'я працівника
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * getLastName - метод для отримання прізвища працівника
     *
     * @return - прізвище працівника
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * setLastName - метод для встановлення прізвища працівника
     *
     * @param lastName - прізвище працівника
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}