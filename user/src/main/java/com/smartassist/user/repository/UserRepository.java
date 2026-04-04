package com.smartassist.user.repository;

import com.smartassist.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    
    // YENİ: Email kontrolü için şart (Service testimizi GREEN yapacak olan bu)
    boolean existsByEmail(String email);

    // Dispatcher için kritik filtreleme
    List<User> findByRoleAndStatus(String role, String status);
}