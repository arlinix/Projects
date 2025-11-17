package com.myshop.service;

import com.myshop.model.User;

public interface UserManagementService {
    String registerUser(User user) throws Exception;
    User[] getUsers() throws Exception;
    User getUserByEmail(String userEmail) throws Exception;
    void clearServiceState() throws Exception;
}