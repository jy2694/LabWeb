package com.example.labweb.controller;

import com.example.labweb.domain.*;
import com.example.labweb.dto.ArticlePostRequestDTO;
import com.example.labweb.service.*;
import lombok.RequiredArgsConstructor;
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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class MainController {

    private ArticleService articleService;
    private LabScheduleService labScheduleService;
    private EmployInfoService employInfoService;
    private ProjectScheduleService projectScheduleService;
    private MemberService memberService;

    @Autowired
    public MainController(ArticleService articleService,
                          LabScheduleService labScheduleService,
                          EmployInfoService employInfoService,
                          ProjectScheduleService projectScheduleService,
                          MemberService memberService){
        this.articleService = articleService;
        this.labScheduleService = labScheduleService;
        this.employInfoService = employInfoService;
        this.projectScheduleService = projectScheduleService;
        this.memberService = memberService;
    }

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

    @GetMapping("/")
    public String mainpage(Model model){
        model.addAttribute("datas", articleService.findLatestArticle("datas", 5));
        model.addAttribute("notices", articleService.findLatestArticle("notice", 5));
        model.addAttribute("employ", employInfoService.findAll());
        model.addAttribute("video", video_list[random.nextInt(video_list.length)]);

        List<ProjectSchedule> projectScheduleList = projectScheduleService.findAll();
        HashMap<String, String> color = new HashMap<>();
        for(ProjectSchedule schedule : projectScheduleList){
            color.put(schedule.getTitle(), colorSet[random.nextInt(colorSet.length)]);
        }
        model.addAttribute("color", color);
        model.addAttribute("projects", projectScheduleList);
        return "index";
    }

    @GetMapping("/contact")
    public String showContacts(Model model){
        model.addAttribute("under", memberService.findAll());
        return "main/info_contact";
    }

    @GetMapping("/{category}")
    public String showNotice(@PathVariable String category, Model model){
        model.addAttribute("category", category);
        model.addAttribute("articles", articleService.findAll(category));
        if(category.equals("notice"))
            return "main/bbs_notice";
        else return "main/bbs_refer";
    }
    @GetMapping("/schedule")
    public String showScheduler(Model model){
        model.addAttribute("schedules", labScheduleService.findAll());
        return "main/info_schedule";
    }

    @GetMapping("/write")
    public String showWritePage(Model model,  @RequestParam("category") String category, @RequestParam("id") String id){
        model.addAttribute("category", category);
        try{
           Long idx = Long.parseLong(id);
           Optional<Article> article = articleService.findById(idx);
           if(article.isPresent()){
               model.addAttribute("title", article.get().getTitle());
               model.addAttribute("content", article.get().getContent());
               model.addAttribute("files", articleService.findAttachmentByBoardId(article.get().getId()));
               model.addAttribute("writer", article.get().getCreatedBy());
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
    public String writeArticle(Model model,String[] exist, Long id, ArticlePostRequestDTO dto){
        if(id == null){
            articleService.postArticle(dto);
        } else {
            Optional<Article> articleOptional = articleService.findById(id);
            if(articleOptional.isPresent()){
                if(articleOptional.get().getPassword().equals(dto.getPassword()))
                    articleService.updateById(id, dto, exist);
                else {
                    model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
                    model.addAttribute("url", "/" + dto.getCategory());
                    return "alert";
                }
            } else {
                articleService.postArticle(dto);
            }
        }
        return "redirect:/" + dto.getCategory();
    }

    @GetMapping("/show")
    public String showArticle(@RequestParam("category") String category, @RequestParam("id") Long id,  Model model){

        Optional<Article> articleOptional = articleService.findById(id);
        if(articleOptional.isEmpty()){
            model.addAttribute("msg", "글이 삭제되었거나 존재하지 않습니다.");
            model.addAttribute("url", "/");
            return "alert";
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

    @PostMapping("/delete")
    public String deleteArticle(String category, String password, Long id, Model model){
        Optional<Article> articleOptional = articleService.findById(id);
        if(articleOptional.isEmpty()){
            model.addAttribute("msg", "글이 삭제되었거나 존재하지 않습니다.");
            model.addAttribute("url", "/" + category);
            return "alert";
        }
        if(!articleOptional.get().getPassword().equals(password)){
            model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("url", "/show?category="+category+"&id=" + id);
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

    @GetMapping("/profiles/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveProfileFile(HttpServletRequest request, @PathVariable String filename){
        Resource file = employInfoService.loadAsResource(filename);
        String userAgent = request.getHeader("User-Agent");
        if(userAgent.indexOf("Trident") > -1)
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20") + "\"").body(file);
        else
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"").body(file);
    }

}
