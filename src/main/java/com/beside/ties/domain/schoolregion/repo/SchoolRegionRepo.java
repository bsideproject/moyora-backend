package com.beside.ties.domain.schoolregion.repo;

import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.schoolregion.entity.SchoolRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SchoolRegionRepo extends JpaRepository<SchoolRegion, Long> {

    List<SchoolRegion> findTop4BySchool_IdAndGraduationYearOrderByCountDesc(Long schoolId, Long graduationYear);

    Optional<SchoolRegion> findBySchool_IdAndRegion_Id(Long schoolId, Long regionId);
    Optional<SchoolRegion> findBySchoolAndRegionAndGraduationYear(School school, Region region, Long graduationYear);

    @Query("select sg from SchoolRegion sg " +
            "left join fetch sg.region sgg " +
            "left join fetch sgg.parent " +
            "where sg.school.id = :schoolId " +
            "and sg.graduationYear = :graduationYear " +
            "order by sg.count desc")
    List<SchoolRegion> findAllBySchool_Id(@Param("schoolId") Long schoolId, @Param("graduationYear") Long graduationYear);

    @Query("select sum(sg.count) from SchoolRegion sg " +
            "where sg.school.id = :schoolId " +
            "and sg.graduationYear = :graduationYear")
    Long totalCountBySchoolId(@Param("schoolId") Long schoolId, @Param("graduationYear") Long graduationYear);
}
