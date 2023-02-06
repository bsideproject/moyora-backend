package com.beside.ties.domain.schooljob.repo;

import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.schooljob.entity.SchoolJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SchoolJobRepo extends JpaRepository<SchoolJob, Long> {

    List<SchoolJob> findTop4BySchool_IdAndGraduationYearOrderByCountDesc(Long schoolId, Long graduationYear);

    Optional<SchoolJob> findBySchool_IdAndJobCategory_Id(Long schoolId, Long jobCategoryId);
    Optional<SchoolJob> findBySchoolAndJobCategoryAndGraduationYear(School school, JobCategory jobCategory, Long graduationYear);


    @Query("select sj from SchoolJob sj " +
            "left join fetch sj.jobCategory sjj " +
            "left join fetch sjj.parent " +
            "where sj.school.id = :schoolId " +
            "and sj.graduationYear = :graduationYear " +
            "order by sj.count desc")
    List<SchoolJob> findAllBySchool_Id(@Param("schoolId") Long schoolId, @Param("graduationYear") Long graduationYear);

    @Query("select sum(sj.count) from SchoolJob sj " +
            "where sj.school.id = :schoolId " +
            "and sj.graduationYear = :graduationYear")
    Long totalCountBySchoolId(@Param("schoolId") Long schoolId, @Param("graduationYear") Long graduationYear);
}
