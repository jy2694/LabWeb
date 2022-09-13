package com.example.labweb.controller;

import com.example.labweb.domain.Article;
import com.example.labweb.domain.GraduateMember;
import com.example.labweb.domain.Member;
import com.example.labweb.domain.ProfMember;
import com.example.labweb.dto.ArticlePostRequestDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class MainController {

    private GraduateMemberService graduateMemberService;
    private MemberService memberService;
    private ProfMemberService profMemberService;
    private ArticleService articleService;
    private LabScheduleService labScheduleService;

    @Autowired
    public MainController(GraduateMemberService graduateMemberRepository,
                          MemberService memberRepository,
                          ProfMemberService profMemberService,
                          ArticleService articleService,
                          LabScheduleService labScheduleService){
        this.graduateMemberService = graduateMemberRepository;
        this.memberService = memberRepository;
        this.profMemberService = profMemberService;
        this.articleService = articleService;
        this.labScheduleService = labScheduleService;
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
    public String editProfile(Model attributes, Principal principal, MemberSignupRequestDTO dto){
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
            attributes.addAttribute("memberName", mi.get().getName());
            attributes.addAttribute("studentId", mi.get().getStudentId());
            attributes.addAttribute("researcherId", mi.get().getResearcherId());
            attributes.addAttribute("email", mi.get().getEmail());
            attributes.addAttribute("phone", mi.get().getPhone());
        } else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()){
                attributes.addAttribute("memberName", gmi.get().getName());
                attributes.addAttribute("studentId", gmi.get().getStudentId());
                attributes.addAttribute("email", gmi.get().getEmail());
                attributes.addAttribute("phone", gmi.get().getPhone());
            } else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                attributes.addAttribute("memberName", pmi.get().getName());
                attributes.addAttribute("email", pmi.get().getEmail());
                attributes.addAttribute("phone", pmi.get().getPhone());
            }
        }
        attributes.addAttribute("ID", principal.getName());
        return "redirect:/profile";
    }

    @GetMapping("/schedule")
    public String showScheduler(Model model, Principal principal){
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
        model.addAttribute("schedules", labScheduleService.findAll());
        return "main/info_schedule";
    }

    @GetMapping("/write")
    public String showWritePage(Model model, Principal principal, @RequestParam("category") String category, @RequestParam("id") String id){
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
        model.addAttribute("category", category);
        try{
           Long idx = Long.parseLong(id);


           Optional<Article> article = articleService.findById(idx);
           if(article.isPresent()){
               model.addAttribute("title", article.get().getTitle());
               model.addAttribute("content", article.get().getContent());
               model.addAttribute("files", articleService.findAttachmentByBoardId(article.get().getId()));
               model.addAttribute("id", idx);
           } else {
               model.addAttribute("id", "new");
           }
        } catch(Exception e){
            model.addAttribute("id", "new");
        }
        model.addAttribute("schedules", labScheduleService.findAll());
        return "main/bbs_write";
    }

    @PostMapping("write")
    public String writeArticle(Model model, Principal principal, String[] exist, Long id, ArticlePostRequestDTO dto){
        if(principal == null) return "redirect:/";
        //Post Article

        if(id == null)
            articleService.postArticle(principal, dto);
        else
            articleService.updateById(id, dto, exist);


        //Member Info
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
        model.addAttribute("articles", articleService.findAll(dto.getCategory()));
        return "redirect:/" + dto.getCategory();
    }
}
