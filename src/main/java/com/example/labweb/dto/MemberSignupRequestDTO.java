package com.example.labweb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSignupRequestDTO {
    private String id;
    private String password;
    private String name;
    private String rs_number;
    private String st_number;
    private String email;
}
