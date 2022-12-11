package com.beside.ties.domain.jobcategory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class JobCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_category_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    JobCategory parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    List<JobCategory> children;

    @Column(nullable = false,unique = true, length = 100)
    String name;

}
