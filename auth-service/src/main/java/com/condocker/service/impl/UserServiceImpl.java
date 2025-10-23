package com.condocker.service.impl;

import com.condocker.dto.Response;
import com.condocker.dto.UserDTO;
import com.condocker.entity.User;
import com.condocker.exceptions.NotFoundException;
import com.condocker.repo.UserRepo;
import com.condocker.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    @Transactional
    public Response<?> updateMyAccount(UserDTO userDTO) {

        User user = currentUser();

        if (userDTO.getName() != null && !userDTO.getName().isBlank()) {
            user.setName(userDTO.getName());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            String password = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(password);
        }

        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cuenta actualizada con exito")
                .build();
    }

    @Override
    public Response<UserDTO> updateUserById(Long id,UserDTO userDTO) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepo.save(user);
        UserDTO updatedUserDTO = modelMapper.map(updatedUser, UserDTO.class);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Usuario actualizado con exito")
                .data(updatedUserDTO)
                .build();
    }

    @Override
    public Response<List<UserDTO>> findAllUsers() {

        List<UserDTO> users = userRepo.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        return Response.<List<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(users.isEmpty() ? "No hay usuarios" : "Usuarios encontrados")
                .data(users)
                .build();
    }

    public Response<?> deleteUser(String email) {
        User user = userRepo.findByEmail(email.trim())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        userRepo.delete(user);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Usuario eliminado con exito")
                .build();
    }
}
