package com.smartinvent.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("adminpass"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("userpass"))
                .roles("USER")
                .build();

        logger.info("Створено користувачів: admin (ADMIN), user (USER)");

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // Захист тільки API-запитів
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/testConnection", "/api/checkTables", "/api/setupDatabase").permitAll() // Доступ для всіх
                        .requestMatchers("/api/config/save", "/api/companies", "/employees").hasRole("ADMIN") // Тільки для ADMIN
                        .requestMatchers("/api/testConnection").hasAnyRole("ADMIN", "USER") // Доступ для всіх авторизованих
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/api/checkTables").authenticated()
                        .anyRequest().authenticated() // Всі інші запити потребують авторизації
                )
                .csrf(csrf -> csrf.disable()) // Відключаємо CSRF для REST API
                .httpBasic(withDefaults()); // Використовуємо Basic Auth

        logger.info("SecurityFilterChain налаштовано!");

        return http.build();
    }
}


//package com.smartinvent.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("adminpass"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("userpass"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/api/**") // Додаємо обмеження для API-шляхів
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/testConnection", "/api/auth/login").permitAll() // Доступ без авторизації
//                        .requestMatchers("/api/config/save", "/api/companies", "/employees").hasRole("ADMIN") // Тільки для ADMIN
//                        .requestMatchers("/api/testConnection").hasAnyRole("ADMIN", "USER") // Доступ для всіх зареєстрованих
//                        .anyRequest().authenticated() // Всі інші запити вимагають авторизації
//                )
//                .csrf(csrf -> csrf.disable()) // Відключаємо CSRF для REST API
//                .httpBasic(withDefaults()); // Використовуємо новий підхід для Basic Auth
//
//        return http.build();
//    }
//}














//package com.smartinvent.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/testConnection").permitAll() // ✅ Дозволяємо доступ до тестового API
//                        .anyRequest().permitAll() // ❗ Всі інші запити також відкриті (якщо потрібна автентифікація — змініть на `.authenticated()`)
//                );
//
//        return http.build();
//    }
//}


//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//
//}


//package com.smartinvent.backend.config;
//
//import com.smartinvent.backend.config.JwtRequestFilter;
//import com.smartinvent.backend.config.CustomUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationManagerResolver;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final JwtRequestFilter jwtRequestFilter;
//    private final CustomUserDetailsService customUserDetailsService;
//
//    public SecurityConfig(JwtRequestFilter jwtRequestFilter, CustomUserDetailsService customUserDetailsService) {
//        this.jwtRequestFilter = jwtRequestFilter;
//        this.customUserDetailsService = customUserDetailsService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http.csrf().disable() // Continue disabling CSRF for stateless JWT
////                .authorizeHttpRequests(authorize -> authorize
////                        .requestMatchers("/api/auth/**").permitAll() // Permit access for authentication routes
////                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Only for ADMIN role
////                        .anyRequest().authenticated() // All other routes require authentication
////                )
////                .sessionManagement(session -> session
////                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session policy
////                )
////                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // Configure security for static resources
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**"); // Static resources
//    }
//}
