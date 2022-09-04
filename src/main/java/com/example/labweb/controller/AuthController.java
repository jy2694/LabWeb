package com.example.labweb.controller;

import com.example.labweb.domain.Member;
import com.example.labweb.domain.MemberInterface;
import com.example.labweb.dto.JwtRequestDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.service.AuthService;
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

@Controller
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    //로그인 페이지에서 포스트 방식으로 ID, PW 전송 받으면 처리되는 메소드
    @PostMapping("/signin")
    public String loginProcess(Model model, JwtRequestDTO dto){
        try{
            MemberInterface member = authService.signin(dto);
            return "main/index";
        } catch(UsernameNotFoundException e){
            model.addAttribute("msg", "사용자가 존재하지 않습니다.");
            model.addAttribute("url", "/");
            return "alert";
        } catch(BadCredentialsException e){
            model.addAttribute("msg", "사용자가 존재하지 않거나 비밀번호가 일치하지 않습니다.");
            model.addAttribute("url", "/");
            return "alert";
        } catch(Exception e){
            model.addAttribute("msg", "데이터베이스 오류입니다.");
            model.addAttribute("url", "/");
            return "alert";
        }
    }

    //회원가입 페이지에서 포스트 방식으로 엔티티 전송 받으면 처리되는 메소드
    @PostMapping("/signup")
    public String registerProcess(Model model, MemberSignupRequestDTO dto){
        if(authService.signup(dto) != null)
            return "auth/login";
        model.addAttribute("msg", "이미 존재하는 아이디입니다.");
        model.addAttribute("url", "/signup");
        return "alert";
    }

    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }
}
