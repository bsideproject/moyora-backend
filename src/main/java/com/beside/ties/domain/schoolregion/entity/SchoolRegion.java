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

    public SchoolRegion(Region region, School school, Long count) {
        this.region = region;
        this.school = school;
        this.count = count;
    }

    public static SchoolRegion createSchoolRegion(Region region, School school, Long count) {
        return new SchoolRegion(region, school, count);
    }
}
