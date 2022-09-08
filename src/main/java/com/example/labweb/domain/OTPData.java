package com.example.labweb.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OTPData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String otpkey;

    @Builder
    public OTPData(Long id, String otpkey) {
        this.id = id;
        this.otpkey = otpkey;
    }

    public OTPData(String otpkey){
        this.otpkey = otpkey;
    }
}
