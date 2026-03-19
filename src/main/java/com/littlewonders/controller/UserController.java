package com.littlewonders.controller;

import com.littlewonders.model.User;
import com.littlewonders.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        
        if (user != null) {
            model.addAttribute("userName", user.getFullName());
            model.addAttribute("regNo", user.getRegistrationNumber());
            model.addAttribute("pendingFees", user.getPendingFees());
        }
        
        return "user/dashboard";
    }

    @GetMapping("/fees")
    public String viewFees(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        model.addAttribute("user", user);
        return "user/fees";
    }
}
