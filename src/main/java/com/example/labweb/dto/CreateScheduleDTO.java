package com.example.labweb.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateScheduleDTO {
    private Long id;
    private Date start;
    private Date end;
    private String title;
    private String description;
    private Boolean allDay;
}
