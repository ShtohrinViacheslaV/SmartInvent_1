package com.smartinvent.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // Тут можна витягнути userId із сесії або заголовків (приклад умовний)
            String userId = httpRequest.getHeader("X-User-Id");
            if (userId == null || userId.isBlank()) {
                userId = "anonymous";
            }

            String sessionId = httpRequest.getSession().getId();
            if (sessionId == null) {
                sessionId = UUID.randomUUID().toString();
            }

            MDC.put("userId", userId);
            MDC.put("sessionId", sessionId);

            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
