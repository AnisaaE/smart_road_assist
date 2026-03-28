package com.smartassist.user.service;

import com.smartassist.user.exception.UserNotFoundException;
import com.smartassist.user.model.User;
import com.smartassist.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   public User createUser(User user) {
        // Repository'deki save metodu MongoRepository'den otomatik gelir
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null); // Eğer kullanıcı yoksa null dön (Şimdilik en basit çözüm)
    }


    public User getUserById(String id) {
    return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(String.format("User with ID [%s] not found", id)));
}

    public void deleteUser(String id) {
        // Önce kullanıcının varlığını kontrol edip yoksa 404 fırlatabilirsin (RMM Level 2)
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Cannot delete. User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    public User updateUser(String id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    user.setPhone(userDetails.getPhone());
                    user.setRole(userDetails.getRole());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException("Update failed. ID: " + id));
    }
}