package com.myshop.repository.impl;

import com.myshop.model.User;
import com.myshop.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    @Override public void save(User user) { if (user != null) users.add(user); }
    @Override public User findByEmail(String email) {
        if (email == null) return null;
        Optional<User> opt = users.stream().filter(u -> email.equalsIgnoreCase(u.getEmail())).findFirst();
        return opt.orElse(null);
    }
    @Override public User[] findAll() { return users.toArray(new User[0]); }
    @Override public void clear() { users.clear(); }
}
