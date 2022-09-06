package com.example.labweb.domain;

import com.example.labweb.dto.MemberSignupRequestDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GraduateMember implements MemberInterface{
    @Id
    private String id;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Builder
    public GraduateMember(String id, String name, String password,
                  String studentId, String email, Date birth, String phone) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.studentId = studentId;
        this.email = email;
        this.birth = birth;
        this.phone = phone;
        this.role = Role.USER;
    }

    public GraduateMember(MemberSignupRequestDTO dto){
        this.id = dto.getId();
        this.name = dto.getName();
        this.password = dto.getPassword();
        this.studentId = dto.getStudentId();
        this.email = dto.getEmail();
        this.birth = dto.getBirth();
        this.phone = dto.getPhone();
        this.role = Role.USER;
    }

    public void encryptPassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

    public boolean isGraduate(){
        return true;
    }

    public boolean isProf(){return false;}
}
