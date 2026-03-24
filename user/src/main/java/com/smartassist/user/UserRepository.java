package com.smartassist.user;

public interface UserRepository {
    void save(User user);
    User findByEmail(String email);
}