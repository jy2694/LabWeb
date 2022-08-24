package com.example.labweb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticlePostRequestDTO {
    private String title;
    private String content;
    private String category;
    private String hashtag;
    private String attached;
}
