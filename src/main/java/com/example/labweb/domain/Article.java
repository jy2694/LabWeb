package com.example.labweb.domain;

import com.example.labweb.dto.ArticlePostRequestDTO;
import lombok.*;

import javax.persistence.*;
import java.security.Principal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private  String createdBy;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false,length = 10000)
    private String content;
    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public Article(String title, String createdBy, String content, String category){
        this.title = title;
        this.createdBy = createdBy;
        this.content = content;
        this.category = category;
    }

    public Article(Principal principal, ArticlePostRequestDTO request){
        this.createdBy = principal.getName();
        this.title = request.getTitle();
        this.content = request.getContent();
        this.category = request.getCategory();
    }
}