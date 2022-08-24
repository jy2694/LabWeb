package com.example.labweb.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
// 도메인 구조 (데이터 설계)
@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false,length = 10000)
    private String content;
    @Column(nullable = false)
    private String category;
    private String attached;

    private String hashtag;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @CreatedBy
    @Column(nullable = false,length = 100)
    private  String createdBy;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;
    @LastModifiedBy
    @Column(nullable = false,length = 100)
    private  String modifiedBy;

    @Builder
    public Article(String title, String content, String hashtag, String category, String attached){
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
        this.category = category;
        this.attached = attached;
    }
}