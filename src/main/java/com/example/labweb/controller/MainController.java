package com.example.labweb.controller;

import com.example.labweb.domain.GraduateMember;
import com.example.labweb.domain.Member;
import com.example.labweb.domain.ProfMember;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.service.ArticleService;
import com.example.labweb.service.GraduateMemberService;
import com.example.labweb.service.MemberService;
import com.example.labweb.service.ProfMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

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
        model.addAttribute("articles", articleService.findAll("data"));
        return "main/bbs_refer";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal){
        if(principal == null) return "error/404";
        Optional<Member> mi = memberService.findById(principal.getName());
        if(mi.isPresent()){
            model.addAttribute("memberName", mi.get().getName());
            model.addAttribute("studentId", mi.get().getStudentId());
            model.addAttribute("researcherId", mi.get().getResearcherId());
            model.addAttribute("email", mi.get().getEmail());
            model.addAttribute("phone", mi.get().getPhone());
        } else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()){
                model.addAttribute("memberName", gmi.get().getName());
                model.addAttribute("studentId", gmi.get().getStudentId());
                model.addAttribute("email", gmi.get().getEmail());
                model.addAttribute("phone", gmi.get().getPhone());
            } else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                model.addAttribute("memberName", pmi.get().getName());
                model.addAttribute("email", pmi.get().getEmail());
                model.addAttribute("phone", pmi.get().getPhone());
            }
        }
        model.addAttribute("ID", principal.getName());
        return "main/profile";
    }

    @PostMapping("/profile")
    public String editProfile(Model model, Principal principal, MemberSignupRequestDTO dto){
        if(dto.getStudentId() != null){
            if(dto.getResearcherId() != null){
                memberService.updateById(dto.getId(), dto);
            } else {
                graduateMemberService.updateById(dto.getId(), dto);
            }
        } else {
            profMemberService.updateById(dto.getId(), dto);
        }
        if(principal == null) return "error/404";
        Optional<Member> mi = memberService.findById(principal.getName());
        if(mi.isPresent()){
            model.addAttribute("memberName", mi.get().getName());
            model.addAttribute("studentId", mi.get().getStudentId());
            model.addAttribute("researcherId", mi.get().getResearcherId());
            model.addAttribute("email", mi.get().getEmail());
            model.addAttribute("phone", mi.get().getPhone());
        } else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()){
                model.addAttribute("memberName", gmi.get().getName());
                model.addAttribute("studentId", gmi.get().getStudentId());
                model.addAttribute("email", gmi.get().getEmail());
                model.addAttribute("phone", gmi.get().getPhone());
            } else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                model.addAttribute("memberName", pmi.get().getName());
                model.addAttribute("email", pmi.get().getEmail());
                model.addAttribute("phone", pmi.get().getPhone());
            }
        }
        model.addAttribute("ID", principal.getName());
        return "main/profile";
    }
}
