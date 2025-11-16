package com.myshop.repository;

import com.myshop.model.User;

public interface UserRepository {
    void save(User user);
    User findByEmail(String email);
    User[] findAll();
    void clear();
}
