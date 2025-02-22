//package com.smartinvent.repositories;
//
//import com.smartinvent.models.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//
//    // Пошук користувача за ім'ям користувача
//    Optional<User> findByUsername(String username);
//
//    // Перевірка наявності користувача за ім'ям користувача
//    boolean existsByUsername(String username);
//
//    // Пошук користувача за email
//    Optional<User> findByEmail(String email);
//}
