package com.smartinvent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Клас-контроллер для обробки запитів, пов'язаних з адміністратором
 */
@Controller
public class AdminController {

    /**
     * Метод для перенаправлення на сторінку адміністратора
     *
     * @return - перенаправлення на сторінку адміністратора
     */
    @GetMapping("/")
    public String redirectToAdmin() {
        return "redirect:/admin/home";
    }

    /**
     * Метод для перенаправлення на сторінку адміністратора
     *
     * @return - перенаправлення на сторінку адміністратора
     */
    @GetMapping("/admin/home")
    public String adminHome() {
        return "fragment_admin_home"; // Тут має бути ваш файл admin_dashboard.html або JSP
    }
}
