package com.beside.ties.domain.school.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.security.Identity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    private Long id = null;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "establish_date")
    private String establishmentDate;

    private String address;
}
