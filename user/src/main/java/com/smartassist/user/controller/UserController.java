package com.smartassist.user.controller;

import com.smartassist.user.model.User;
import com.smartassist.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users") // RMM Level 2: Kaynak bazlı URI
public class UserController {

    private final UserService userService;

    // Dependency Injection: Spring bu servisi otomatik bağlayacak
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping // RMM Level 2: Create işlemi için doğru HTTP metodu
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Gerçek mantığı servise devrediyoruz
        User createdUser = userService.createUser(user);
        
        // RMM Level 2: Başarılı kayıt sonrası 201 Created dönüyoruz
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}