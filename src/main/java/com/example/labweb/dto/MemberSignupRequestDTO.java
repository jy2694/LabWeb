package com.example.labweb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSignupRequestDTO {
    private String id;
    private String password;
    private String name;
    private String researcherId;
    private String studentId;
    private String email;
    private boolean graduate;
}
