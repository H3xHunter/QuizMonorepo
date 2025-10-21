package com.condocker.eureka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class EurekaSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF completamente
                .csrf(AbstractHttpConfigurer::disable)
                // Permitir acceso a TODOS los endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .anyRequest().permitAll()
                )
                // Deshabilitar autenticación básica
                .httpBasic(AbstractHttpConfigurer::disable)
                // Deshabilitar formulario de login
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}