package com.reservas.reservasBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.reservas.reservasBackend.Jwt.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class ReservasBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReservasBackendApplication.class, args);
    }
}