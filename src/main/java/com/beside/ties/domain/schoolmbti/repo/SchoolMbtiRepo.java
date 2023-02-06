package com.beside.ties.domain.schoolmbti.repo;


import com.beside.ties.domain.mbti.entity.Mbti;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.schoolmbti.entity.SchoolMbti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SchoolMbtiRepo extends JpaRepository<SchoolMbti, Long> {

    List<SchoolMbti> findTop4BySchool_IdOrderByCountDesc(Long schoolId);

    Optional<SchoolMbti> findBySchool_IdAndMbti_Id(Long schoolId, Long mbtiId);

    Optional<SchoolMbti> findBySchoolAndMbtiAndGraduationYear(School school, Mbti mbti, Long graduationYear);

    @Query("select sm from SchoolMbti sm " +
            "left join fetch sm.mbti smm " +
            "where sm.school.id = :schoolId " +
            "order by sm.count desc")
    List<SchoolMbti> findAllBySchool_Id(@Param("schoolId") Long schoolId);

    @Query("select sum(sm.count) from SchoolMbti sm where sm.school.id = :schoolId")
    Long totalCountBySchoolId(@Param("schoolId") Long schoolId);
}
