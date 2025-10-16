package com.condocker.eureka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class EurekaSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF para Eureka dashboard
                .csrf(csrf -> csrf.disable())
                // Permitir acceso a todos los endpoints de Eureka
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/eureka/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                // Deshabilitar autenticación básica
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}