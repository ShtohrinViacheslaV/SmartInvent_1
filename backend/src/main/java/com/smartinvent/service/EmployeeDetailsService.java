package com.smartinvent.service;

import com.smartinvent.models.Employee;
import com.smartinvent.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmployeeWorkId(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача " + username + " не знайдено"));

        return User.builder()
                .username(employee.getEmployeeWorkId())
                .password(employee.getPasswordHash()) // Пароль уже має бути хешованим у БД!
                .roles(employee.getRole()) // Роль беремо з БД
                .build();
    }
}
