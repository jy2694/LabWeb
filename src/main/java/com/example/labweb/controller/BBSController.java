package com.example.labweb.controller;

import com.example.labweb.domain.Article;
import com.example.labweb.service.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BBSController {

    private ArticleService articleService;

    @GetMapping("/bbs/{postid}")
    public Article getArticle(@PathVariable("postid") Long id){
        return articleService.getArticle(id);
    }

    @PostMapping("/bbs")
    public Article postArticle(){

    }
}
