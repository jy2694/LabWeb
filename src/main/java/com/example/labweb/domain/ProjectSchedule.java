package com.example.labweb.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity

public class ProjectSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*일정시작 시간*/
    @Column(nullable = false)
    private Date start;
    /*일정종료 시간*/
    @Column(nullable = false)
    private Date end;
    /*일정 제목*/
    @Column(nullable = false)
    private String title;
    /*일정 참여 인원*/
    @Column(nullable = false)
    private String personnel;

    public ProjectSchedule(Date start, Date end, String title, String personnel){
        this.start = start;
        this.end = end;
        this.title = title;
        this.personnel = personnel;
    }
}
