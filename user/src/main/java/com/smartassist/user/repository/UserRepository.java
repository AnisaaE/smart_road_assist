package com.smartassist.user.repository;

import com.smartassist.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // Spring Data "findBy" + "Email" isminden otomatik sorgu üretir.
    Optional<User> findByEmail(String email); 
}