package com.example.labweb.domain;

import com.example.labweb.dto.MemberSignupRequestDTO;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="member")
public class MemberEntity {
    @Id
    private String id;
    private String password;
    private String name;
    private String rs_number;
    private String st_number;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public MemberEntity(String id, String name, String password,
                String rs_number, String st_number, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.rs_number = rs_number;
        this.st_number = st_number;
        this.email = email;
        this.role = Role.USER;
    }

    public MemberEntity(MemberSignupRequestDTO dto){
        this.id = dto.getId();
        this.name = dto.getName();
        this.password = dto.getPassword();
        this.rs_number = dto.getRs_number();
        this.st_number = dto.getSt_number();
        this.email = dto.getEmail();
        this.role = Role.USER;
    }

    public void encryptPassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }
}
