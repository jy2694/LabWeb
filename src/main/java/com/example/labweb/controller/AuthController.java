package com.example.labweb.controller;

import com.example.labweb.domain.GraduateMember;
import com.example.labweb.domain.Member;
import com.example.labweb.domain.MemberInterface;
import com.example.labweb.domain.ProfMember;
import com.example.labweb.dto.JwtRequestDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.service.AuthService;
import com.example.labweb.service.GraduateMemberService;
import com.example.labweb.service.MemberService;
import com.example.labweb.service.ProfMemberService;
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
import java.security.Principal;
import java.util.Optional;

@Controller
public class AuthController {

    private final AuthService authService;
    private GraduateMemberService graduateMemberService;
    private MemberService memberService;
    private ProfMemberService profMemberService;
    @Autowired
    public AuthController(AuthService authService,
                          GraduateMemberService graduateMemberRepository,
                          MemberService memberRepository,
                          ProfMemberService profMemberService){
        this.authService = authService;
        this.graduateMemberService = graduateMemberRepository;
        this.memberService = memberRepository;
        this.profMemberService = profMemberService;
    }

    //로그인 페이지에서 포스트 방식으로 ID, PW 전송 받으면 처리되는 메소드
    @PostMapping("/")
    public String loginProcess(Model model, JwtRequestDTO dto){
        try{
            MemberInterface member = authService.signin(dto);
            model.addAttribute("memberName", member.getName());
            if(member instanceof Member){
                model.addAttribute("studentId", ((Member) member).getStudentId());
            } else if(member instanceof GraduateMember){
                model.addAttribute("studentId", ((GraduateMember) member).getStudentId());
            }
            model.addAttribute("ROLE", member.getRole());
            return "index";
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

    @GetMapping("/out")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @GetMapping("/")
    public String showLoginPage(Model model, Principal principal){
        if(principal == null)
            return "/auth/login";
        Optional<Member> mi = memberService.findById(principal.getName());
        if(mi.isPresent()){
            model.addAttribute("memberName", mi.get().getName());
            model.addAttribute("studentId", mi.get().getStudentId());
            model.addAttribute("ROLE", mi.get().getRole());
        } else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()){
                model.addAttribute("memberName", gmi.get().getName());
                model.addAttribute("studentId", gmi.get().getStudentId());
                model.addAttribute("ROLE", gmi.get().getRole());
            } else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                model.addAttribute("memberName", pmi.get().getName());
                model.addAttribute("ROLE", pmi.get().getRole());
            }
        }
       return "index";
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
