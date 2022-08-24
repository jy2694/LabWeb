package com.example.labweb.service;

import com.example.labweb.domain.Article;
import com.example.labweb.dto.ArticlePostRequestDTO;
import com.example.labweb.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ArticleService {
    private ArticleRepository articleRepository;

    public Article postArticle(Principal principal, ArticlePostRequestDTO request){
        Article article = new Article(principal, request);
        return save(article);
    }

    public List<Article> findAll(String category){
        return articleRepository.findAll().stream()
                .filter(article -> article.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public Optional<Article> findById(Long id){
        return articleRepository.findById(id);
    }

    public List<Article> findByContainedString(String category, String string){
        return articleRepository.findAll().stream()
                .filter(article -> article.getCategory().equals(category))
                .filter(article -> article.getContent().contains(string))
                .collect(Collectors.toList());
    }

    public Article save(Article article){
        articleRepository.save(article);
        return article;
    }

    public void deleteById(Long id){
        articleRepository.deleteById(id);
    }

    public Optional<Article> updateById(Long id, ArticlePostRequestDTO dto){
        Optional<Article> e = articleRepository.findById(id);
        e.ifPresent(article -> {
            if(dto.getTitle() != null)
                article.setTitle(dto.getTitle());
            if(dto.getCategory() != null)
                article.setCategory(dto.getCategory());
            if(dto.getContent() != null)
                article.setContent(dto.getContent());
            if(dto.getAttached() != null)
                article.setAttached(dto.getAttached());
            if(dto.getHashtag() != null)
                article.setHashtag(dto.getHashtag());
            articleRepository.save(article);
        });
        return e;
    }
}
