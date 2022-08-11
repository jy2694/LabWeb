package com.example.labweb.vo;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mbrNo;

    private String id;
    private String password;
    private String name;
    private String rs_number;
    private String st_number;
    private String email;

    @Builder
    public MemberEntity(String id, String name, String password,
                String rs_number, String st_number, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.rs_number = rs_number;
        this.st_number = st_number;
        this.email = email;
    }
}
