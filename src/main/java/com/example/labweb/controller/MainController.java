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
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

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
            "2022-09-05_pepper.mp4",
            "2022-10-05_LINC.mp4"
    };
    private final SecureRandom random = new SecureRandom();

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
    public String showNotice(@PathVariable String category, @RequestParam(value = "page", required = false) Long page, Model model) throws IOException {
        List<Article> articles = articleService.findAll(category).stream().collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
            Collections.reverse(list);
            return list.stream();
        })).collect(Collectors.toList());
        model.addAttribute("category", category);
        if(category.equals("notice")) {
            model.addAttribute("articles", articles);
            return "main/bbs_notice";
        } else if(category.equals("gallery")){
            if(page == null){
                model.addAttribute("status", "404");
                model.addAttribute("error", "Page Not Found");
                return "error/404";
            }
            List<Article> filterArticles = new ArrayList<>();
            for(long i = (page-1) * 8; i < (page)*8; i ++){
                if(i >= articles.size()) break;
                filterArticles.add(articles.get((int)i));
            }
            model.addAttribute("articles", filterArticles);

            long maxPage = (articles.size() / 8) + 1;

            long start = page - 2;
            long end = page + 2;

            if(maxPage <= 5){
                start = 1;
                end = maxPage;
            } else {
                if(start < 1){
                    while(start > 0){
                        start ++;
                        end ++;
                    }
                }
                if(end > maxPage){
                    while(end <= maxPage){
                        start --;
                        end --;
                    }
                }
            }
            List<Long> pagelist = new ArrayList<>();
            for(long i = start; i <= end; i ++)
                pagelist.add(i);
            model.addAttribute("pages", pagelist);
            HashMap<Long, String> thumbnails = new HashMap<>();
            for(Article article : articles){
                List<Attachment> attachments = articleService.findAttachmentByBoardId(article.getId());
                for(Attachment attachment : attachments){
                    if(articleService.fileUploadCheckJpg(attachment)){
                        thumbnails.put(article.getId(), attachment.getOriginPath());
                        break;
                    }
                }
            }
            model.addAttribute("thumbnails", thumbnails);
            return "main/gallery";
        } else if(category.equals("data")){
            model.addAttribute("articles", articles);
            return "main/bbs_refer";
        }
        model.addAttribute("status", "404");
        model.addAttribute("error", "Page Not Found");
        return "error/404";
    }

    @GetMapping("/intro")
    public String showIntro(Model model){
        //TODO - 연구실 소개글
        return "main/introduction";
    }

    @GetMapping("/eachintro")
    public String showEachIntro(Model model){

        return "main/each_intro";
    }

    @GetMapping("/history")
    public String showLabHistory(Model model){

        return "main/lab_history";
    }

    @GetMapping("/schedule")
    public String showScheduler(Model model){
        model.addAttribute("schedules", labScheduleService.findAll());
        return "main/info_schedule";
    }

    @GetMapping("/write")
    public String showWritePage(Model model,  @RequestParam("category") String category, @RequestParam("id") String id){
        model.addAttribute("category", category);
        Long idx = Long.parseLong(id);
        if(idx != null){
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
        } else {
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
        if(dto.getCategory().equals("gallery")){
            return "redirect:/"+dto.getCategory()+"?page=1";
        }
        return "redirect:/" + dto.getCategory();
    }

    @GetMapping("/show")
    public String showArticle(@RequestParam("category") String category, @RequestParam("id") Long id, Model model){

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
        if(category.equals("gallery")){
            List<Attachment> images = attachmentList.stream().filter(attachment ->
                articleService.fileUploadCheckJpg(attachment)
            ).collect(Collectors.toList());
            model.addAttribute("images", images);
            return "main/gallery_extend";
        }
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
        if(category.equals("gallery")){
            return "redirect:/"+category+"?page=1";
        }
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
