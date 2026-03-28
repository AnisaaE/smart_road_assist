package com.smartassist.user.service;

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
    // TDD kuralı: Şimdilik içi boş, sadece derleme hatasını çözüyoruz
    return null; 
}
}