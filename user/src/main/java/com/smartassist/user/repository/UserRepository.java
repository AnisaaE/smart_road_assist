package com.smartassist.user.repository;

import com.smartassist.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    // YENİ: Role ve Status'a göre filtreleme (Dispatcher için kritik)
    List<User> findByRoleAndStatus(String role, String status);
}