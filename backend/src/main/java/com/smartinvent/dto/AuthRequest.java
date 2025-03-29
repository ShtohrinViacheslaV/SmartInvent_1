package com.smartinvent.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Клас-запит для авторизації користувача
 */
@Getter
@Setter
public class AuthRequest {

    /**
     * Поле для зберігання email
     */
    private String email;

    /**
     * Поле для зберігання пароля
     */
    private String password;

    /**
     * getEmail - метод для отримання email
     *
     * @return - email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setEmail - метод для встановлення email
     *
     * @param email - email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getPassword - метод для отримання пароля
     *
     * @return - пароль
     */
    public String getPassword() {
        return password;
    }

    /**
     * setPassword - метод для встановлення пароля
     *
     * @param password - пароль
     */
    public void setPassword(String password) {
        this.password = password;
    }
}