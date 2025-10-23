package com.condocker.controller;

import com.condocker.dto.Response;
import com.condocker.dto.UserDTO;
import com.condocker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<Response<?>> updateMyAccount(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateMyAccount(userDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<UserDTO>> updateUserById(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserById(id, userDTO));
    }

    @GetMapping("/find-all")
    public ResponseEntity<Response<List<UserDTO>>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping("/delete")
    public ResponseEntity<Response<?>> deleteUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return ResponseEntity.ok(userService.deleteUser(email));
    }
}

