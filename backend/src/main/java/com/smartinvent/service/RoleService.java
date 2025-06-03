//package com.smartinvent.service;
//
//import com.smartinvent.models.Role;
//import com.smartinvent.repositories.RoleRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class RoleService {
//
//    private final RoleRepository roleRepository;
//
//    // Отримання всіх ролей
//    public List<Role> getAllRoles() {
//        return roleRepository.findAll();
//    }
//
//    // Отримання ролі за ім'ям
//    public Role getRoleByName(String name) {
//        return roleRepository.findByName(name)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//    }
//
//    // Створення нової ролі
//    public Role createRole(Role role) {
//        if (roleRepository.findByName(role.getName()).isPresent()) {
//            throw new RuntimeException("Role already exists");
//        }
//        return roleRepository.save(role);
//    }
//
//    // Оновлення ролі
//    public Role updateRole(Long roleId, Role updatedRole) {
//        Role existingRole = roleRepository.findById(roleId)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//
//        existingRole.setName(updatedRole.getName());
//        existingRole.setDescription(updatedRole.getDescription());
//
//        return roleRepository.save(existingRole);
//    }
//
//    // Видалення ролі
//    public void deleteRole(Long roleId) {
//        Role role = roleRepository.findById(roleId)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//        roleRepository.delete(role);
//    }
//}
