package com.myshop.controller;

import com.myshop.model.DefaultUser;
import com.myshop.model.User;
import com.myshop.repository.jdbc.UserRepositoryJdbcImpl;
import com.myshop.service.UserManagementService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserManagementService userService;
    private final UserRepositoryJdbcImpl userRepo;

    @Autowired
    public AuthController(UserManagementService userService, UserRepositoryJdbcImpl userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        try {
            String hash = userRepo.getPasswordHashByEmail(email);
            if (hash != null && BCrypt.checkpw(password, hash)) {
                User user = userService.getUserByEmail(email);
                session.setAttribute("user", user);
                return "redirect:/";
            }
        } catch (Exception ex) {
            model.addAttribute("error", "Login error: " + ex.getMessage());
            return "login";
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() { return "signup"; }

    @PostMapping("/signup")
    public String signup(@RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String email,
                         @RequestParam String password,
                         Model model) {
        try {
            DefaultUser du = new DefaultUser();
            du.setFirstName(firstName);
            du.setLastName(lastName);
            du.setEmail(email);
            du.setPassword(password);
            String res = userService.registerUser(du);
            if (res != null && res.startsWith("New user")) {
                return "redirect:/auth/login";
            } else {
                model.addAttribute("error", res);
                return "signup";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Signup failed: " + e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
