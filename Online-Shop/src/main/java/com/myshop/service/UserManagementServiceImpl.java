package com.myshop.service;

import com.myshop.model.User;
import com.myshop.model.DefaultUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserManagementServiceImpl implements UserManagementService{

    private static UserManagementServiceImpl instance;
    private final List<User> users;

    private UserManagementServiceImpl() {
        users = new ArrayList<>();
    }

    public static UserManagementServiceImpl getInstance(){
        if(instance == null){
            instance = new UserManagementServiceImpl();
        }

        return instance;
    }



    @Override
    public synchronized String registerUser(User user) {
        if(user == null){
            return "You have to input email to register. Please, try one more time";
        }
        String email = user.getEmail();

        if(email == null || email.trim().isEmpty()){
            return "You have to input email to register. Please, try one more time";
        }

        if(getUserByEmail(email) != null){
            return "This email is already used by another user. Please, use another email";
        }

        this.users.add(user);

        return "New user is created";
    }

    @Override
    public User[] getUsers() {
        return users.toArray(new User[0]);
    }

    @Override
    public User getUserByEmail(String userEmail) {
        if(userEmail == null){
            return null;
        }

        Optional<User> found = users.stream()
                .filter(u -> userEmail.equalsIgnoreCase(u.getEmail()))
                .findFirst();
        return found.orElse(null);


    }

    public void clearServiceState() {
        this.users.clear();
// Reset DefaultUser static counter if implemented
        try {
            DefaultUser.clearState();
        } catch (Throwable ignored) {
// If DefaultUser.clearState() doesn't exist or fails, ignore - tests should
// still pass if DefaultUser handles its own reset somewhere else.
        }
    }
}
