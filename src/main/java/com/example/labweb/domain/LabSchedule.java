package com.example.labweb.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LabSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String start;

    @Column(nullable = false)
    private String  end;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public LabSchedule(String start, String end, String title, String content){
        this.start = start;
        this.end = end;
        this.title = title;
        this.content = content;
    }
}