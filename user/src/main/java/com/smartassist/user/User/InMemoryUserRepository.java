package com.smartassist.user.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {
    private Map<String, User> storage = new HashMap<>();

    @Override
    public void save(User user) {
        storage.put(user.getEmail(), user);
    }

    @Override
    public User findByEmail(String email) {
        return storage.get(email);
    }
}