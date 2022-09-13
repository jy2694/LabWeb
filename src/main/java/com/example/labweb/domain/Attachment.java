package com.example.labweb.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileExtension;

    @Column(nullable = false)
    private String originPath;

    @Column(nullable = false)
    private Long fileSize;

    @Builder
    public Attachment(Long boardId, String fileName, String fileExtension, String originPath, Long fileSize){
        this.boardId = boardId;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.originPath = originPath;
        this.fileSize = fileSize;
    }

}
