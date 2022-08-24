package com.example.labweb.controller;

import com.example.labweb.domain.Article;
import com.example.labweb.dto.ArticlePostRequestDTO;
import com.example.labweb.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class BBSController {

    private ArticleService articleService;

    @Autowired
    public BBSController(ArticleService articleService){
        this.articleService = articleService;
    }

    @GetMapping("/bbs/view/{id}")
    public Article getArticle(@PathVariable("id") Long id){
        //TODO - NULL CHECK
        return articleService.findById(id).get();
    }

    @GetMapping("/bbs/{category}")
    public List<Article> getArticles(@PathVariable("category") String category){
        return articleService.findAll(category);
    }

    @GetMapping("/bbs/search/{category}/{string}")
    public List<Article> getArticles(@PathVariable("category") String category, @PathVariable("string") String string){
        return articleService.findByContainedString(category, string);
    }

    @DeleteMapping("/bbs/view/{id}")
    public void deleteArticle(@PathVariable("id") Long id){
        articleService.deleteById(id);
    }

    @PutMapping("/bbs/view/{id}")
    public Article modifyArticle(@PathVariable("id") Long id, ArticlePostRequestDTO dto){
        //TODO - NULL CHECK
        return articleService.updateById(id, dto).get();
    }

    @PostMapping("/bbs")
    public Article postArticle(Principal principal, ArticlePostRequestDTO dto){
        return articleService.postArticle(principal, dto);
    }
}
