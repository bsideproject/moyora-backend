package com.beside.ties.domain.schooljob.repo;

import com.beside.ties.domain.schooljob.entity.SchoolJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SchoolJobRepo extends JpaRepository<SchoolJob, Long> {

    List<SchoolJob> findTop4BySchool_IdOrderByCountDesc(Long schoolId);

    Optional<SchoolJob> findBySchool_IdAndJobCategory_Id(Long schoolId, Long jobCategoryId);

    @Query("select sj from SchoolJob sj " +
            "left join fetch sj.jobCategory sjj " +
            "left join fetch sjj.parent " +
            "where sj.school.id = :schoolId " +
            "order by sj.count desc")
    List<SchoolJob> findAllBySchool_Id(@Param("schoolId") Long schoolId);

    @Query("select sum(sj.count) from SchoolJob sj where sj.school.id = :schoolId")
    Long totalCountBySchoolId(@Param("schoolId") Long schoolId);
}
