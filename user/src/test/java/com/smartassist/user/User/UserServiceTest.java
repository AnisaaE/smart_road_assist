package com.smartassist.user.User;

import org.junit.jupiter.api.Test;

import com.smartassist.user.User.InMemoryUserRepository;
import com.smartassist.user.User.User;
import com.smartassist.user.User.UserRepository;
import com.smartassist.user.User.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    void shouldCreateUserSuccessfully() {
        UserRepository userRepository = new InMemoryUserRepository();
        UserService userService = new UserService(userRepository);

        User user = new User("Beyza", "beyza@example.com");
        userService.createUser(user);

        User savedUser = userRepository.findByEmail("beyza@example.com");
        assertNotNull(savedUser);
        assertEquals("Beyza", savedUser.getName());

        System.out.println("Kaydedilen kullanıcı: " + savedUser.getName());
        assertEquals("Beyza", savedUser.getName());
    }

    @Test
    void shouldFindUserByEmail() {
        UserRepository userRepository = new InMemoryUserRepository();
        UserService userService = new UserService(userRepository);

        User user = new User("Beyza", "beyza@example.com");
        userRepository.save(user);

        User foundUser = userService.getUserByEmail("beyza@example.com");
        assertNotNull(foundUser);
        assertEquals("Beyza", foundUser.getName());
    }
}