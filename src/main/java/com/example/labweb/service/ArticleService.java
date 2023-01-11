package com.example.labweb.service;

import com.example.labweb.configuration.StorageProperties;
import com.example.labweb.domain.Article;
import com.example.labweb.domain.Attachment;
import com.example.labweb.dto.ArticlePostRequestDTO;
import com.example.labweb.repository.ArticleRepository;
import com.example.labweb.repository.AttachmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ArticleService {
    private ArticleRepository articleRepository;
    private AttachmentRepository attachmentRepository;
    private StorageProperties properties;
    public Article postArticle(ArticlePostRequestDTO request){
        try{
            Article article = new Article(request);
            articleRepository.save(article);
            if(request.getAttached() != null){
                for(MultipartFile file : request.getAttached()){
                    if(file.isEmpty()) continue;
                    String fileName = file.getOriginalFilename();
                    String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                    String originPath = UUID.randomUUID().toString().replaceAll("-","") + fileExtension;
                    Long fileSize = file.getSize();

                    Path destinationFile = Paths.get(properties.getLocation()).resolve(
                                    Paths.get(originPath))
                            .normalize().toAbsolutePath();
                    try (InputStream inputStream = file.getInputStream()) {
                        Files.copy(inputStream, destinationFile,
                                StandardCopyOption.REPLACE_EXISTING);
                    }
                    attachmentRepository.save(new Attachment(
                            article.getId(),
                            fileName,
                            fileExtension,
                            originPath,
                            fileSize
                    ));
                }
            }
            return article;
        } catch(IOException e){
            return null;
        }
    }

    public List<Article> findAll(String category){
        return articleRepository.findAll().stream()
                .filter(article -> article.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public List<Attachment> findAttachmentByBoardId(Long boardId){
        return attachmentRepository.findByBoardId(boardId);
    }

    public Optional<Article> findById(Long id){
        return articleRepository.findById(id);
    }

    public List<Article> findLatestArticle(String category, int count){
        List<Article> articles = articleRepository.findAll().stream()
                .filter(e -> e.getCategory().equals(category))
                .sorted(Comparator.comparing(Article::getCreatedAt).reversed())
                .collect(Collectors.toList());
        if(articles.size() > count)
            return articles.subList(0, count);
        else
            return articles;
    }

    public Article save(Article article){
        articleRepository.save(article);
        return article;
    }

    public void deleteById(Long id){
        for(Attachment attachment : attachmentRepository.findByBoardId(id))
            deleteAttachment(attachment.getId());
        articleRepository.deleteById(id);
    }

    public void deleteAttachment(Long id){
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if(optionalAttachment.isEmpty())return;
        Path destinationFile = Paths.get(properties.getLocation()).resolve(
                        Paths.get(optionalAttachment.get().getOriginPath()))
                .normalize().toAbsolutePath();
        destinationFile.toFile().delete();
        attachmentRepository.deleteById(id);
    }

    public Optional<Article> updateById(Long id, ArticlePostRequestDTO dto, String[] exist){
        Optional<Article> e = articleRepository.findById(id);
        e.ifPresent(article -> {
            if(dto.getTitle() != null)
                article.setTitle(dto.getTitle());
            if(dto.getCategory() != null)
                article.setCategory(dto.getCategory());
            if(dto.getContent() != null)
                article.setContent(dto.getContent());
            articleRepository.save(article);
        });

        List<Attachment> now = attachmentRepository.findByBoardId(id);
        for(Attachment attachment : now){
            if(exist != null) {
                if (!isContained(exist, attachment))
                    deleteAttachment(attachment.getId());
            } else deleteAttachment(attachment.getId());
        }
        try{
            if(dto.getAttached() != null){
                for(MultipartFile file : dto.getAttached()){
                    if(file.isEmpty()) continue;
                    String fileName = file.getOriginalFilename();
                    String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                    String originPath = UUID.randomUUID().toString().replaceAll("-","") + fileExtension;
                    Long fileSize = file.getSize();

                    Path destinationFile = Paths.get(properties.getLocation()).resolve(
                                    Paths.get(originPath))
                            .normalize().toAbsolutePath();
                    try (InputStream inputStream = file.getInputStream()) {
                        Files.copy(inputStream, destinationFile,
                                StandardCopyOption.REPLACE_EXISTING);
                    }
                    attachmentRepository.save(new Attachment(
                            id,
                            fileName,
                            fileExtension,
                            originPath,
                            fileSize
                    ));
                }
            }
        } catch(IOException e1){
            return null;
        }
        return e;
    }

    public Attachment findByOriginPath(String path){
        List<Attachment> attachments = attachmentRepository.findByOriginPath(path);
        if(attachments.size() == 0) return null;
        return attachments.get(0);
    }

    public Resource loadAsResource(String filename){
        try {
            Path file = Paths.get(properties.getLocation()).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else
                return null;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private boolean isContained(String[] exist, Attachment attachment){
        for(String line : exist)
            if(Integer.parseInt(line) == attachment.getId())
                return true;
        return false;
    }

    public boolean fileUploadCheckJpg(Attachment attachment){
        boolean result= false;
        if(attachment.getFileExtension().equalsIgnoreCase(".jpg")
                ||attachment.getFileExtension().equalsIgnoreCase(".bmp")
                ||attachment.getFileExtension().equalsIgnoreCase(".gif")
                ||attachment.getFileExtension().equalsIgnoreCase(".png")
                ||attachment.getFileExtension().equalsIgnoreCase(".jpeg")){
            result = true;
        }

        return result;

    }
}
