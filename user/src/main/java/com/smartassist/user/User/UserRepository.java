package com.smartassist.user.User;

public interface UserRepository {
    void save(User user);
    User findByEmail(String email);
}