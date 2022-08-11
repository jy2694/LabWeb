package com.example.labweb.controller;

import com.example.labweb.service.MemberService;
import com.example.labweb.vo.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class SignController {

    private MemberService memberService;

    @Autowired
    public SignController(MemberService memberService){
        this.memberService = memberService;
    }

    //접속 시 보여지는 로그인 페이지
    @GetMapping("/")
    public String loginMapper(Model model){
        //TODO - 로그인 페이지
        return "/sign/login";
    }

    //접속 시 보여지는 회원가입 페이지
    @GetMapping("/register")
    public String registerMapper(Model model){
        //TODO - 회원가입 페이지
        return "sign/register";
    }

    //로그인 페이지에서 포스트 방식으로 ID, PW 전송 받으면 처리되는 메소드
    @PostMapping("/")
    public String loginprocess(String ID, String PW){
        Optional<MemberEntity> member = memberService.findById(ID);
        if(member.isPresent()) {
            if(member.get().getPassword().equals(PW)) {
                return "/main/index";
            }
        }
        return "/sign/login";
    }

    //회원가입 페이지에서 포스트 방식으로 엔티티 전송 받으면 처리되는 메소드
    @PostMapping("/register")
    public String registerprocess(MemberEntity entity){
        memberService.save(entity);
        return "/sign/login";
    }
}
