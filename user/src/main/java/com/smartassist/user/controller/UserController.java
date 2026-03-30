package com.smartassist.user.controller;

import com.smartassist.user.dto.UserRequestDTO; // EKLE
import com.smartassist.user.dto.UserResponseDTO; // EKLE
import com.smartassist.user.exception.UserNotFoundException;
import com.smartassist.user.service.IUserService; // UserService yerine IUserService
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService; // Artık Interface kullanıyoruz (SOLID)

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(UserNotFoundException ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("message", ex.getMessage());
        errorBody.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") String id) {
        // IUserService.getUserById artık UserResponseDTO döner
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping 
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request) {
        // Parametre artık UserRequestDTO olmalı
        UserResponseDTO createdUser = userService.createUser(request);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") String id, @RequestBody UserRequestDTO request) {
        // User yerine UserRequestDTO kullanıyoruz
        UserResponseDTO updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }
}