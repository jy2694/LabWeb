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
    private String studentId;
    @Column(nullable = false)
    private String company;

    @Builder
    public EmployInfo(Long id, String name, String studentId, String company){
        this.id = id;
        this.name = name;
        this.studentId = studentId;
        this.company = company;
    }
}
