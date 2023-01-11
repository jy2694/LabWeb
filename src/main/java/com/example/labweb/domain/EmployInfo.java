package com.example.labweb.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class EmployInfo {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String company;
    @Column(nullable = false)
    private String profileName;

    @Builder
    public EmployInfo(Long id, String name, String company, String profileName){
        this.id = id;
        this.name = name;
        this.company = company;
        this.profileName = profileName;
    }
}
