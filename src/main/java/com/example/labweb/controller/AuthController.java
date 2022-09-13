package com.example.labweb.controller;

import com.example.labweb.api.GoogleOtpAPI;
import com.example.labweb.domain.*;
import com.example.labweb.dto.JwtRequestDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;

@Controller
public class AuthController {

    private final AuthService authService;
    private GraduateMemberService graduateMemberService;
    private MemberService memberService;
    private ProfMemberService profMemberService;
    private ArticleService articleService;
    private OTPDataService otpDataService;
    private EmployInfoService employInfoService;
    private ProjectScheduleService projectScheduleService;
    private final String[] colorSet = {
            "bg-info",
            "bg-primary",
            "bg-success",
            "bg-warning",
            "bg-danger"
    };
    private final String[] video_list = {
            "2022-08-13_capstone.mp4",
            "2022-09-05_pepper.mp4"
    };
    private final Random random = new Random();

    @Autowired
    public AuthController(AuthService authService,
                          GraduateMemberService graduateMemberRepository,
                          MemberService memberRepository,
                          ProfMemberService profMemberService,
                          ArticleService articleService,
                          OTPDataService otpDataService,
                          EmployInfoService employInfoService,
                          ProjectScheduleService projectScheduleService){
        this.authService = authService;
        this.graduateMemberService = graduateMemberRepository;
        this.memberService = memberRepository;
        this.profMemberService = profMemberService;
        this.articleService = articleService;
        this.otpDataService = otpDataService;
        this.employInfoService = employInfoService;
        this.projectScheduleService = projectScheduleService;
    }

    //로그인 페이지에서 포스트 방식으로 ID, PW 전송 받으면 처리되는 메소드
    @PostMapping("/")
    public String loginProcess(Model rttr, JwtRequestDTO dto){
        try{
            MemberInterface member = authService.signin(dto);
            rttr.addAttribute("memberName", member.getName());
            if(member instanceof Member){
                rttr.addAttribute("studentId", ((Member) member).getStudentId());
            } else if(member instanceof GraduateMember){
                rttr.addAttribute("studentId", ((GraduateMember) member).getStudentId());
            }
            rttr.addAttribute("ROLE", member.getRole());

            //BBS Data
            rttr.addAttribute("datas", articleService.findLatestArticle("data", 5));
            rttr.addAttribute("notices", articleService.findLatestArticle("notice", 5));
            rttr.addAttribute("employ", employInfoService.findAll());

            rttr.addAttribute("video", video_list[random.nextInt(video_list.length)]);

            //Research Data
            List<ProjectSchedule> projectScheduleList = projectScheduleService.findAll();

            HashMap<String, String> color = new HashMap<>();
            for(ProjectSchedule schedule : projectScheduleList){
                color.put(schedule.getTitle(), colorSet[random.nextInt(colorSet.length)]);
            }
            rttr.addAttribute("color", color);
            rttr.addAttribute("projects", projectScheduleList);

            return "redirect:/";
        } catch(UsernameNotFoundException e){
            rttr.addAttribute("msg", "사용자가 존재하지 않습니다.");
            rttr.addAttribute("url", "/");
            return "alert";
        } catch(BadCredentialsException e){
            rttr.addAttribute("msg", "사용자가 존재하지 않거나 비밀번호가 일치하지 않습니다.");
            rttr.addAttribute("url", "/");
            return "alert";
        } catch(Exception e){
            rttr.addAttribute("msg", "데이터베이스 오류입니다.");
            rttr.addAttribute("url", "/");
            return "alert";
        }
    }

    //회원가입 페이지에서 포스트 방식으로 엔티티 전송 받으면 처리되는 메소드
    @PostMapping("/signup")
    public String registerProcess(Model rttr, MemberSignupRequestDTO dto){
        int result = 0;
        if((result = authService.signup(dto)) == 0)
            return "redirect:/";
        if(result == 1){
            rttr.addAttribute("msg", "이미 계정이 존재합니다.");
            rttr.addAttribute("url", "/signup");
        } else if(result == 2){
            rttr.addAttribute("msg", "OTP가 일치하지 않습니다.");
            rttr.addAttribute("url", "/signup");
        }
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
        model.addAttribute("datas", articleService.findLatestArticle("data", 5));
        model.addAttribute("notices", articleService.findLatestArticle("notice", 5));
        model.addAttribute("employ", employInfoService.findAll());

        model.addAttribute("video", video_list[random.nextInt(video_list.length)]);

        //Research Data
        List<ProjectSchedule> projectScheduleList = projectScheduleService.findAll();

        HashMap<String, String> color = new HashMap<>();
        for(ProjectSchedule schedule : projectScheduleList){
            color.put(schedule.getTitle(), colorSet[random.nextInt(colorSet.length)]);
        }
        model.addAttribute("color", color);
        model.addAttribute("projects", projectScheduleList);
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

    @PostMapping("/otp")
    public String changeOtpSettingPage(RedirectAttributes rttr, Principal principal){
        if(principal == null)
            return "/error/404";
        Optional<Member> mi = memberService.findById(principal.getName());
        Role role;
        if(mi.isPresent()) role = mi.get().getRole();
        else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()) role = gmi.get().getRole();
            else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                role = pmi.get().getRole();
            }
        }
        if(otpDataService.getOTPData().isPresent() && role != Role.ADMIN)
            return "/error/404";
        HashMap<String, String> otpdata = GoogleOtpAPI.generate("연구실정보시스템","LABWEB");
        otpDataService.changeOTPData(otpdata.get("key"));
        rttr.addAttribute("otpUrl", otpdata.get("url"));
        return "redirect:/otp";
    }

    @GetMapping("/otp")
    public String showOtpSettingPage(Principal principal){
        if(principal == null)
            return "/error/404";
        Optional<Member> mi = memberService.findById(principal.getName());
        Role role;
        if(mi.isPresent()) role = mi.get().getRole();
        else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()) role = gmi.get().getRole();
            else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                role = pmi.get().getRole();
            }
        }
        if(!otpDataService.getOTPData().isPresent()) return "/auth/otp";
        if(role != Role.ADMIN)
            return "/error/404";
        return "/auth/otp";
    }
}
