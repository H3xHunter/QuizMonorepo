package com.condocker.service;


import com.condocker.dto.Response;
import com.condocker.dto.UserDTO;
import com.condocker.entity.User;

import java.util.List;

public interface UserService {

    User currentUser();

    Response<?> updateMyAccount(UserDTO userDTO);

    Response<UserDTO> updateUserById(Long id, UserDTO userDTO);

    Response<List<UserDTO>> findAllUsers();

    Response<?> deleteUser(String email);
}
