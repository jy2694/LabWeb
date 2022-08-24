package com.example.labweb.controller;

import com.example.labweb.domain.Member;
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
@RestController
public class AuthController {

    private final AuthService authService;

    //로그인 페이지에서 포스트 방식으로 ID, PW 전송 받으면 처리되는 메소드
    @PostMapping("/auth/login")
    public Member loginprocess(JwtRequestDTO dto){
        try{
            Member member = authService.signin(dto);
            return member;
        } catch(UsernameNotFoundException e){
            return null;
        } catch(BadCredentialsException e){
            return null;
        } catch(Exception e){
            return null;
        }
    }

    //회원가입 페이지에서 포스트 방식으로 엔티티 전송 받으면 처리되는 메소드
    @PostMapping("/auth/register")
    public Member registerprocess(MemberSignupRequestDTO dto){
        Member member = null;
        if((member = authService.signup(dto)) != null)
            return member;
        return null;
    }

    @GetMapping("logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }
}
