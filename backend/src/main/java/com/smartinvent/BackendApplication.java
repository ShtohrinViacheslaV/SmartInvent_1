package com.smartinvent;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "SmartInvent API", version = "1.0", description = "API для інвентаризації"))

public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

    }

}
