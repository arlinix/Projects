package com.myshop.service;

import com.myshop.model.User;

public interface UserManagementService {
    String registerUser(User user);

    User[] getUsers();

    User getUserByEmail(String userEmail);

}
