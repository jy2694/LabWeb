package com.example.labweb.controller;

import com.example.labweb.dto.JwtRequestDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService authService;

    private final String loginpage = "/auth/login",
            registerpage = "/auth/register",
            alert = "alert";

    //접속 시 보여지는 로그인 페이지
    @GetMapping("/")
    public String loginMapper(Model model){
        //TODO - 로그인 페이지
        //model.addAttribute("msg","");
        return loginpage;
    }

    //접속 시 보여지는 회원가입 페이지
    @GetMapping("/register")
    public String registerMapper(Model model){
        //TODO - 회원가입 페이지
        return registerpage;
    }

    @GetMapping("/passchange")
    public String changeMapper(Model model){
        return "/auth/passchange";
    }

    //로그인 페이지에서 포스트 방식으로 ID, PW 전송 받으면 처리되는 메소드
    @PostMapping("/")
    public String loginprocess(Model model, JwtRequestDTO dto){
        try{
            authService.signin(dto);
            return "/main/index";
        } catch(UsernameNotFoundException e){
            model.addAttribute("msg","사용자 계정을 찾을 수 없습니다.");
            return loginpage;
        } catch(BadCredentialsException e){
            model.addAttribute("msg","사용자 계정이 존재하지 않거나 비밀번호가 일치하지 않습니다.");
            return loginpage;
        } catch(Exception e){
            model.addAttribute("msg","알 수 없는 오류입니다.");
            return loginpage;
        }
    }

    //회원가입 페이지에서 포스트 방식으로 엔티티 전송 받으면 처리되는 메소드
    @PostMapping("register")
    public String registerprocess(Model model, MemberSignupRequestDTO dto){
        if(authService.signup(dto) != null)
            return loginpage;
        model.addAttribute("msg","이미 존재하는 계정입니다.");
        model.addAttribute("url","/register");
        return alert;
    }

    @GetMapping("logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }
}
