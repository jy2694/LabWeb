package com.example.labweb.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class MemberSignupRequestDTO {
    private String id;
    private String password;
    private String name;
    private String researcherId;
    private String studentId;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    private String phone;
}
