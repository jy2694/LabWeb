package com.example.labweb.domain;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface MemberInterface {
    void encryptPassword(PasswordEncoder passwordEncoder);

    Role getRole();

    String getId();

    String getPassword();

    boolean isGraduate();
}
