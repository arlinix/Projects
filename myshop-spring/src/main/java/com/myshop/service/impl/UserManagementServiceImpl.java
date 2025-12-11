package com.myshop.service.impl;

import com.myshop.model.DefaultUser;
import com.myshop.model.User;
import com.myshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Service
public class UserManagementServiceImpl implements com.myshop.service.UserManagementService {

    private final UserRepository userRepository;

    @Autowired
    public UserManagementServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String registerUser(User user) throws Exception {
        if (user == null) return "You have to input email to register. Please, try one more time";
        String email = user.getEmail();
        if (email == null || email.trim().isEmpty()) return "You have to input email to register. Please, try one more time";
        try (Connection conn = ((com.myshop.repository.jdbc.UserRepositoryJdbcImpl)userRepository).conn()) {
            if (userRepository.findByEmail(conn, email) != null) return "This email is already used by another user. Please, use another email";
            DefaultUser du = (DefaultUser) user;
            userRepository.save(conn, du);
            return "New user is created";
        }
    }

    @Override
    public User[] getUsers() throws Exception {
        try (Connection conn = ((com.myshop.repository.jdbc.UserRepositoryJdbcImpl)userRepository).conn()) {
            List<DefaultUser> list = userRepository.findAll(conn);
            return list.toArray(new User[0]);
        }
    }

    @Override
    public User getUserByEmail(String userEmail) throws Exception {
        try (Connection conn = ((com.myshop.repository.jdbc.UserRepositoryJdbcImpl)userRepository).conn()) {
            return userRepository.findByEmail(conn, userEmail);
        }
    }

    @Override
    public void clearServiceState() throws Exception {
        // no-op
    }
}
