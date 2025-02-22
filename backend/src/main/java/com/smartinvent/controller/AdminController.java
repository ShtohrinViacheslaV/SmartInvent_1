package com.smartinvent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/")
    public String redirectToAdmin() {
        return "redirect:/admin/home";
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "fragment_admin_home"; // Тут має бути ваш файл admin_dashboard.html або JSP
    }
}
