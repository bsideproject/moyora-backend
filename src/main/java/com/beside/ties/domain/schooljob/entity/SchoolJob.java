package com.beside.ties.domain.schooljob.entity;

import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.schoolregion.entity.SchoolRegion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class SchoolJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_job_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    private JobCategory jobCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @Column(name = "count")
    private Long count;

    public SchoolJob(JobCategory jobCategory, School school, Long count) {
        this.jobCategory = jobCategory;
        this.school = school;
        this.count = count;
    }

    public SchoolJob(JobCategory jobCategory, School school) {
        this.jobCategory = jobCategory;
        this.school = school;
        this.count = 1L;
    }

    public void plusOneCount() {
        this.count = this.count + 1L;
    }
    public void minusOneCount() {
        this.count = this.count - 1L;
    }

    public static SchoolJob createSchoolJob(JobCategory jobCategory, School school, Long count) {
        return new SchoolJob(jobCategory, school, count);
    }
}
