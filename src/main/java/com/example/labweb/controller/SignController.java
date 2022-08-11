package com.example.labweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class SignController {

    @GetMapping("/sign/login")
    public String loginMapper(Model model){
        //TODO - 로그인 페이지
        return "/sign/login";
    }

    @GetMapping("/sign/register")
    public String registerMapper(Model model){
        //TODO - 회원가입 페이지
        return "/sign/register";
    }

    @PostMapping("")
    public void loginpro(String userId, String userPw){

        //userId db 조회
        //존재하면 비밀번호도 조회
        //if(Id ==){

    }


}
