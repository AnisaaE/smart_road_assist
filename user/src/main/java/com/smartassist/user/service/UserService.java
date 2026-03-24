package com.smartassist.user.service;

import com.smartassist.user.model.User;
import com.smartassist.user.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null); // Eğer kullanıcı yoksa null dön (Şimdilik en basit çözüm)
    }
}