package com.example.labweb.service;

import com.example.labweb.domain.Article;
import com.example.labweb.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ArticleService {
    private ArticleRepository articleRepository;

    public List<Article> getAllArticle(String category){
        return articleRepository.findAll().stream()
                .filter(article -> article.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public Article getArticle(Long id){
        Optional<Article> article = articleRepository.findById(id);
        if(article.isPresent())
            return article.get();
        else return null;
    }


}
