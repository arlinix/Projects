package com.myshop.service.impl;

import com.myshop.model.DefaultUser;
import com.myshop.model.User;
import com.myshop.db.DbConnectionFactory;
import com.myshop.repository.UserRepository;
import com.myshop.service.UserManagementService;

import java.sql.Connection;
import java.util.List;

public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;

    public UserManagementServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String registerUser(User user) throws Exception {
        if (user == null) return "You have to input email to register. Please, try one more time";
        String email = user.getEmail();
        if (email == null || email.trim().isEmpty()) return "You have to input email to register. Please, try one more time";
        try (Connection conn = DbConnectionFactory.getConnection()) {
            if (userRepository.findByEmail(conn, email) != null) return "This email is already used by another user. Please, use another email";
            DefaultUser du = (DefaultUser) user;
            userRepository.save(conn, du);
            return "New user is created";
        }
    }

    @Override
    public User[] getUsers() throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            List<DefaultUser> list = userRepository.findAll(conn);
            return list.toArray(new User[0]);
        }
    }

    @Override
    public User getUserByEmail(String userEmail) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            return userRepository.findByEmail(conn, userEmail);
        }
    }

    @Override
    public void clearServiceState() throws Exception {
        // no in-memory caches to clear in DB-backed impl
    }
}