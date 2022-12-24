package com.beside.ties.domain.jobcategory.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class JobCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_category_id")
    private Long id;

    @Column(nullable = false,unique = true, length = 100)
    private String name;

    public JobCategory(
           String name
    ){
        this.name = name;
    }

}
