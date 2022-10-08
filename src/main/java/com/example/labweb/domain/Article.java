package com.example.labweb.domain;

import com.example.labweb.dto.ArticlePostRequestDTO;
import lombok.*;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String password;

    @Builder
    public Article(String title, String createdBy, String content, String category, String writer, String password){
        this.title = title;
        this.createdBy = createdBy;
        this.content = content;
        this.category = category;
        this.createdBy = writer;
        this.password = password;
    }

    public Article(ArticlePostRequestDTO request){
        this.createdBy = request.getWriter();
        this.title = request.getTitle();
        this.content = request.getContent();
        this.category = request.getCategory();
        this.password = request.getPassword();
    }
}