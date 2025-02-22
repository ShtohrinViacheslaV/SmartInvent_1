package com.smartinvent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


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
