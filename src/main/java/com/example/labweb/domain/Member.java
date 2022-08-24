package com.example.labweb.domain;

import com.example.labweb.dto.MemberSignupRequestDTO;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {
    @Id
    private String id;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String researcherId;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Builder
    public Member(String id, String name, String password,
                  String researcherId, String studentId, String email,
                  boolean graduate) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.researcherId = researcherId;
        this.studentId = studentId;
        this.email = email;
        this.role = Role.USER;
    }

    public Member(MemberSignupRequestDTO dto){
        this.id = dto.getId();
        this.name = dto.getName();
        this.password = dto.getPassword();
        this.researcherId = dto.getResearcherId();
        this.studentId = dto.getStudentId();
        this.email = dto.getEmail();
        this.role = Role.USER;
    }

    public void encryptPassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }
}
