package com.beside.ties.domain.jobcategory.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
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
    public String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private JobCategory parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<JobCategory> children;

    public JobCategory(
           String name
    ){
        this.name = name;
    }

    public JobCategory(
            String name,
            JobCategory jobCategory
    ){
        this.name = name;
        this.parent = jobCategory;
    }

    public void setParent(JobCategory parent){
        this.parent = parent;
    }


}
