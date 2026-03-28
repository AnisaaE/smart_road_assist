package com.smartassist.user.controller;

import com.smartassist.user.model.User;
import com.smartassist.user.service.UserService;
import lombok.RequiredArgsConstructor; // 1. BU IMPORTU EKLE
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor // 2. BU, CONSTRUCTOR'I OTOMATİK OLUŞTURUR
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) { // Explicit path variable
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping 
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    
    // 3. ELİNLE YAZDIĞIN CONSTRUCTOR'I BURADAN SİLDİK
}