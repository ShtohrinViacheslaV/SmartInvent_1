//package com.smartinvent.controller;
//
//import com.smartinvent.models.Role;
//import com.smartinvent.service.RoleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/roles")
//@RequiredArgsConstructor
//public class RoleController {
//
//    private final RoleService roleService;
//
//    // Отримання всіх ролей
//    @GetMapping("/all")
//    public List<Role> getAllRoles() {
//        return roleService.getAllRoles();
//    }
//
//    // Отримання ролі за ім'ям
//    @GetMapping("/{name}")
//    public Role getRoleByName(@PathVariable String name) {
//        return roleService.getRoleByName(name);
//    }
//
//    // Створення нової ролі
//    @PostMapping("/create")
//    public Role createRole(@RequestBody Role role) {
//        return roleService.createRole(role);
//    }
//
//    // Оновлення ролі
//    @PutMapping("/update/{roleId}")
//    public Role updateRole(@PathVariable Long roleId, @RequestBody Role role) {
//        return roleService.updateRole(roleId, role);
//    }
//
//    // Видалення ролі
//    @DeleteMapping("/delete/{roleId}")
//    public void deleteRole(@PathVariable Long roleId) {
//        roleService.deleteRole(roleId);
//    }
//
//
//}
