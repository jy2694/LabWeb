package com.example.labweb.controller;

import com.example.labweb.domain.GraduateMember;
import com.example.labweb.domain.Member;
import com.example.labweb.domain.MemberInterface;
import com.example.labweb.domain.ProfMember;
import com.example.labweb.repository.GraduateMemberRepository;
import com.example.labweb.repository.MemberRepository;
import com.example.labweb.repository.ProfMemberRepository;
import com.example.labweb.service.ArticleService;
import com.example.labweb.service.GraduateMemberService;
import com.example.labweb.service.MemberService;
import com.example.labweb.service.ProfMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private GraduateMemberService graduateMemberService;
    private MemberService memberService;
    private ProfMemberService profMemberService;
    private ArticleService articleService;

    @Autowired
    public MainController(GraduateMemberService graduateMemberRepository,
                          MemberService memberRepository,
                          ProfMemberService profMemberService,
                          ArticleService articleService){
        this.graduateMemberService = graduateMemberRepository;
        this.memberService = memberRepository;
        this.profMemberService = profMemberService;
        this.articleService = articleService;
    }

    @GetMapping("/contact")
    public String showContacts(Model model, Principal principal){
        if(principal == null) return "error/404";
        Optional<Member> mi = memberService.findById(principal.getName());
        if(mi.isPresent()){
            model.addAttribute("memberName", mi.get().getName());
            model.addAttribute("studentId", mi.get().getStudentId());
        } else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()){
                model.addAttribute("memberName", gmi.get().getName());
                model.addAttribute("studentId", gmi.get().getStudentId());
            } else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                model.addAttribute("memberName", pmi.get().getName());
            }
        }
        model.addAttribute("under", memberService.findAll());
        model.addAttribute("grad", graduateMemberService.findAll());
        return "main/info_contact";
    }

    @GetMapping("/notice")
    public String showNotice(Model model, Principal principal){
        if(principal == null) return "error/404";
        Optional<Member> mi = memberService.findById(principal.getName());
        if(mi.isPresent()){
            model.addAttribute("memberName", mi.get().getName());
            model.addAttribute("studentId", mi.get().getStudentId());
        } else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()){
                model.addAttribute("memberName", gmi.get().getName());
                model.addAttribute("studentId", gmi.get().getStudentId());
            } else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                model.addAttribute("memberName", pmi.get().getName());
            }
        }
        model.addAttribute("articles", articleService.findAll("notice"));
        return "main/bbs_notice";
    }

    @GetMapping("/data")
    public String showData(Model model, Principal principal){
        if(principal == null) return "error/404";
        Optional<Member> mi = memberService.findById(principal.getName());
        if(mi.isPresent()){
            model.addAttribute("memberName", mi.get().getName());
            model.addAttribute("studentId", mi.get().getStudentId());
        } else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()){
                model.addAttribute("memberName", gmi.get().getName());
                model.addAttribute("studentId", gmi.get().getStudentId());
            } else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                model.addAttribute("memberName", pmi.get().getName());
            }
        }
        model.addAttribute("articles", articleService.findAll("data"));
        return "main/bbs_refer";
    }

}
