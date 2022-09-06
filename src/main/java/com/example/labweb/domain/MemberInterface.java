package com.example.labweb.domain;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;

public interface MemberInterface {
    void encryptPassword(PasswordEncoder passwordEncoder);

    Role getRole();

    String getId();

    String getPassword();

    String getName();

    Date getBirth();

    String getPhone();

    boolean isGraduate();

    boolean isProf();
}
