package com.littlewonders.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/ipsm-admin")
    public String adminLogin() {
        return "admin-login";
    }
}
