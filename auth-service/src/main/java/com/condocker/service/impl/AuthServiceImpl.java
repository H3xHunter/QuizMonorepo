package com.condocker.service.impl;


import com.condocker.dto.LoginRequest;
import com.condocker.dto.LoginResponse;
import com.condocker.dto.RegistrationRequest;
import com.condocker.dto.Response;
import com.condocker.entity.User;
import com.condocker.exceptions.BadRequestException;
import com.condocker.repo.UserRepo;
import com.condocker.security.JwtUtils;
import com.condocker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public Response<?> register(RegistrationRequest registrationRequest) {
        if(userRepo.existsByEmail(registrationRequest.getEmail())) {
            throw new BadRequestException("El email ya existe");
        }

        User userToSave = new User();
        userToSave.setName(registrationRequest.getName());
        userToSave.setEmail(registrationRequest.getEmail());
        userToSave.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userToSave.setCreatedAt(LocalDateTime.now());
        userToSave.setUpdatedAt(LocalDateTime.now());
        userToSave.setActive(true);

        userRepo.save(userToSave);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Usuario registrado exitosamente")
                .build();
    }

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) {

        User user = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("Credenciales invalidas"));

        if(!user.isActive()){
            throw new BadRequestException("Usuario inactivo");
        }

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new BadRequestException("Password incorrecto");
        }

        String token = jwtUtils.generateToken(user.getEmail());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);

        return Response.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login exitoso")
                .data(loginResponse)
                .build();
    }
}