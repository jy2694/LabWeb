package com.example.labweb.domain;

import com.example.labweb.dto.CreateScheduleDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LabSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date start;

    @Column(nullable = false)
    private Date end;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean allDay;

    @Builder
    public LabSchedule(Date start, Date end, String title, String description, boolean allDay){
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.allDay = allDay;
    }

    public LabSchedule(CreateScheduleDTO dto){
        this.start = dto.getStart();
        this.end = dto.getEnd();
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.allDay = dto.getAllDay();
    }
}