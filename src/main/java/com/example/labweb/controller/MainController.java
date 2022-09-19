package com.example.labweb.controller;

import com.example.labweb.domain.*;
import com.example.labweb.dto.ArticlePostRequestDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
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

    @GetMapping("/{category}")
    public String showNotice(@PathVariable String category, Model model, Principal principal){
        if(principal == null) return "error/404";
        if(!category.equals("notice") && !category.equals("data")) return "redirect:/";
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
        model.addAttribute("articles", articleService.findAll(category));
        if(category.equals("notice"))
            return "main/bbs_notice";
        else return "main/bbs_refer";
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
        String name, studentId = null;
        Role role;
        if(mi.isPresent()){
            name = mi.get().getName();
            studentId = mi.get().getStudentId();
            role = mi.get().getRole();
        } else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()){
                name = gmi.get().getName();
                studentId = gmi.get().getStudentId();
                role = gmi.get().getRole();
            } else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                name = pmi.get().getName();
                role = pmi.get().getRole();
            }
        }
        model.addAttribute("category", category);
        try{
           Long idx = Long.parseLong(id);
           Optional<Article> article = articleService.findById(idx);
           if(article.isPresent()){

               if(!role.equals(Role.ADMIN) && !article.get().getCreatedBy().equals(principal.getName())){
                   model.addAttribute("msg", "권한이 없습니다.");
                   model.addAttribute("url", "/");
                   return "alert";
               }

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
        model.addAttribute("memberName", name);
        if(studentId != null)
            model.addAttribute("studentId", studentId);
        model.addAttribute("ROLE", role);
        model.addAttribute("schedules", labScheduleService.findAll());
        return "main/bbs_write";
    }

    @PostMapping("write")
    public String writeArticle(Model model, Principal principal, String[] exist, Long id, ArticlePostRequestDTO dto){
        if(principal == null) return "redirect:/";
        Optional<Member> mi = memberService.findById(principal.getName());
        String name, studentId = null;
        Role role;
        if(mi.isPresent()){
            name = mi.get().getName();
            studentId = mi.get().getStudentId();
            role = mi.get().getRole();
        } else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()){
                name = gmi.get().getName();
                studentId = gmi.get().getStudentId();
                role = gmi.get().getRole();
            } else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                name = pmi.get().getName();
                role = pmi.get().getRole();
            }
        }

        //Post Article

        if(id == null)
            articleService.postArticle(principal, dto);
        else{
            Optional<Article> articleOptional = articleService.findById(id);
            if(articleOptional.isEmpty())
                articleService.postArticle(principal, dto);
            else {
                if(!role.equals(Role.ADMIN) && !articleOptional.get().getCreatedBy().equals(principal.getName())){
                    model.addAttribute("msg", "권한이 없습니다.");
                    model.addAttribute("url", "/");
                    return "alert";
                }
                articleService.updateById(id, dto, exist);
            }
        }




        model.addAttribute("memberName", name);
        if(studentId != null)
            model.addAttribute("studentId", studentId);
        model.addAttribute("ROLE", role);
        model.addAttribute("articles", articleService.findAll(dto.getCategory()));
        return "redirect:/" + dto.getCategory();
    }

    @GetMapping("/show")
    public String showArticle(@RequestParam("category") String category, @RequestParam("id") Long id,  Model model, Principal principal){
        if(principal == null)
            return "redirect:/";
        model.addAttribute("memberId", principal.getName());
        Optional<Article> articleOptional = articleService.findById(id);
        if(articleOptional.isEmpty()){
            model.addAttribute("msg", "글이 삭제되었거나 존재하지 않습니다.");
            model.addAttribute("url", "/");
            return "alert";
        }

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

        List<Attachment> attachmentList = articleService.findAttachmentByBoardId(id);
        Article article = articleOptional.get();
        model.addAttribute("title", article.getTitle());
        model.addAttribute("content", article.getContent());
        model.addAttribute("author", article.getCreatedBy());
        model.addAttribute("files", attachmentList);
        model.addAttribute("id", id);
        model.addAttribute("category", category);
        return "main/bbs_content";
    }

    @GetMapping("/delete")
    public String deleteArticle(@RequestParam("category") String category, @RequestParam("id") Long id, Model model, Principal principal){
        if(principal == null)
            return "redirect:/";
        Optional<Article> articleOptional = articleService.findById(id);
        if(articleOptional.isEmpty()){
            model.addAttribute("msg", "글이 삭제되었거나 존재하지 않습니다.");
            model.addAttribute("url", "/" + category);
            return "alert";
        }

        Optional<Member> mi = memberService.findById(principal.getName());
        Role role;
        if(mi.isPresent()){
            role = mi.get().getRole();
        } else {
            Optional<GraduateMember> gmi = graduateMemberService.findById(principal.getName());
            if(gmi.isPresent()){
                role = gmi.get().getRole();
            } else {
                Optional<ProfMember> pmi = profMemberService.findById(principal.getName());
                role = pmi.get().getRole();
            }
        }

        if(!role.equals(Role.ADMIN) && !articleOptional.get().getCreatedBy().equals(principal.getName())){
            model.addAttribute("msg", "권한이 없습니다.");
            model.addAttribute("url", "/show?category=" + category + "&id=" + id);
            return "alert";
        }

        articleService.deleteById(id);

        return "redirect:/" + category;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(HttpServletRequest request, @PathVariable String filename) {
        Attachment attachments = articleService.findByOriginPath(filename);
        Resource file = articleService.loadAsResource(filename);
        String userAgent = request.getHeader("User-Agent");
        if(userAgent.indexOf("Trident") > -1)
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + URLEncoder.encode(attachments.getFileName(),StandardCharsets.UTF_8).replaceAll("\\+", "%20") + "\"").body(file);
        else
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + new String(attachments.getFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"").body(file);
    }

}
