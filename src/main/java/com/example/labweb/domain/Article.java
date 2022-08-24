package com.example.labweb.domain;

import com.example.labweb.dto.ArticlePostRequestDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.security.Principal;
import java.time.LocalDateTime;

@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
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
    private String attached;

    private String hashtag;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public Article(String title, String createdBy, String content, String hashtag, String category, String attached){
        this.title = title;
        this.createdBy = createdBy;
        this.content = content;
        this.hashtag = hashtag;
        this.category = category;
        this.attached = attached;
    }

    public Article(Principal principal, ArticlePostRequestDTO request){
        this.createdBy = principal.getName();
        this.title = request.getTitle();
        this.content = request.getContent();
        this.hashtag = request.getHashtag();
        this.category = request.getCategory();
        this.attached = request.getAttached();
    }
}