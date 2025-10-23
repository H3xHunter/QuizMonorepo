package com.condocker.service;


import com.condocker.dto.LoginRequest;
import com.condocker.dto.LoginResponse;
import com.condocker.dto.RegistrationRequest;
import com.condocker.dto.Response;

public interface AuthService {

    Response<?> register(RegistrationRequest registrationRequest);

    Response<LoginResponse> login(LoginRequest loginRequest);
}
