package com.example.labweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String showLoginPage(){
        return "/auth/login";
    }

    @GetMapping({"/signup", "/signup/under"})
    public String showRegisterPage(){
        return "/auth/register";
    }

    @GetMapping("/signup/grad")
    public String showGRegisterPage(){
        return "/auth/g_register";
    }

    @GetMapping("/signup/prof")
    public String showPRegisterPage(){
        return "/auth/p_register";
    }
}
