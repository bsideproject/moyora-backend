package com.beside.ties.domain.school.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    private Long id = null;

    @Column(name = "school_name")
    public String schoolName;

    @Column(name = "establish_date")
    private String establishmentDate;

    private String address;

    @Column(name = "school_code", unique = true)
    private String schoolCode;

    @Builder
    public School(
            String schoolName,
            String establishmentDate,
            String address,
            String schoolCode
    ){
        this.schoolName = schoolName;
        this.establishmentDate = establishmentDate;
        this.address = address;
        this.schoolCode = schoolCode;
    }
}
