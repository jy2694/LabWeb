package com.example.labweb.controller;

import com.example.labweb.dto.JwtRequestDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.service.AuthService;
import com.example.labweb.service.MemberService;
import com.example.labweb.domain.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService authService;

    private final String loginpage = "/auth/LabWeb",
            registerpage = "/auth/register";

    //접속 시 보여지는 로그인 페이지
    @GetMapping("/")
    public String loginMapper(Model model){
        //TODO - 로그인 페이지
        return loginpage;
    }

    //접속 시 보여지는 회원가입 페이지
    @GetMapping("/register")
    public String registerMapper(Model model){
        //TODO - 회원가입 페이지
        return registerpage;
    }

    //로그인 페이지에서 포스트 방식으로 ID, PW 전송 받으면 처리되는 메소드
    @PostMapping("/")
    public String loginprocess(JwtRequestDTO dto){
        try{
            authService.signin(dto);
            return "/main/index";
        } catch(UsernameNotFoundException e){
            System.out.println("Username Not found");
            return loginpage;
        } catch(BadCredentialsException e){
            System.out.println("Wrong password");
            return loginpage;
        } catch(Exception e){
            System.out.println("Unknown Error");
            return loginpage;
        }
    }

    //회원가입 페이지에서 포스트 방식으로 엔티티 전송 받으면 처리되는 메소드
    @PostMapping("register")
    public String registerprocess(MemberSignupRequestDTO dto){
        if(authService.signup(dto) != null)
            return "/auth/login";
        return registerpage;
    }

    @GetMapping("logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }
}
