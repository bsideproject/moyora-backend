package com.beside.ties.domain.schoolregion.repo;

import com.beside.ties.domain.schoolregion.entity.SchoolRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchoolRegionRepo extends JpaRepository<SchoolRegion, Long> {

    List<SchoolRegion> findTop4BySchool_IdOrderByCountDesc(Long schoolId);

    @Query("select sg from SchoolRegion sg join fetch sg.region where sg.school.id = :schoolId order by sg.count desc")
    List<SchoolRegion> findAllBySchool_Id(@Param("schoolId") Long schoolId);

    @Query("select sum(sg.count) from SchoolRegion sg where sg.school.id = :schoolId")
    Long totalCountBySchoolId(@Param("schoolId") Long schoolId);
}