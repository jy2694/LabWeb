package com.example.labweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    /**
     * Sign(로그인, 회원가입)
     * 로그인, 메인화면, 회원가입, 게시판(공지사항, 자료실, 갤러리), 스케쥴러, 졸업자
     */
    @GetMapping(value = {"/main", "/index"})
    public String mainPageMapper(){
        return "/main/index";
    }
}
