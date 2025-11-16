package com.myshop.service.impl;

import com.myshop.model.User;
import com.myshop.model.DefaultUser;
import com.myshop.repository.UserRepository;
import com.myshop.service.UserManagementService;

public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;

    public UserManagementServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String registerUser(User user) {
        if (user == null) return "You have to input email to register. Please, try one more time";
        String email = user.getEmail();
        if (email == null || email.trim().isEmpty()) return "You have to input email to register. Please, try one more time";
        if (userRepository.findByEmail(email) != null) return "This email is already used by another user. Please, use another email";
        userRepository.save(user);
        return "New user is created";
    }

    @Override public User[] getUsers() { return userRepository.findAll(); }

    @Override public User getUserByEmail(String userEmail) { return userRepository.findByEmail(userEmail); }

    @Override
    public void clearServiceState() {
        userRepository.clear();
        try { DefaultUser.clearState(); } catch (Throwable ignored) {}
    }
}
