package com.beside.ties.domain.schoolregion.entity;

import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.school.entity.School;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class SchoolRegion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_region_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @Column(name = "count")
    private Long count;
}
