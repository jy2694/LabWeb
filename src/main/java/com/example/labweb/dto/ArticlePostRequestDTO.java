package com.example.labweb.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ArticlePostRequestDTO {
    private String title;
    private String content;
    private String category;
    private MultipartFile[] attached;
}
