package com.beside.ties.domain.statistics.repo;

import com.beside.ties.domain.account.entity.QAccount;
import com.beside.ties.domain.common.dto.StatisticsDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.beside.ties.domain.account.entity.QAccount.account;
@Repository
@Transactional
@RequiredArgsConstructor
public class StatisticsRepo {

    private final JPAQueryFactory jpaQueryFactory;

    public List<StatisticsDto> regionStatistics(Long schoolId, int graduationYear) {
        StringPath aliasValue = Expressions.stringPath("value");

        return jpaQueryFactory.select(Projections.bean(StatisticsDto.class,
                        account.region.parent.name.concat(" ").concat(account.region.name).as("title"),
                        account.count().as("value")
                ))
                .from(account)
                .where(
                        schoolIdEq(schoolId),
                        graduationYearEq(graduationYear),
                        account.graduationYear.ne(0)
                )
                .groupBy(account.graduationYear, account.school, account.region)
                .orderBy(aliasValue.desc())
                .fetch();
    }

    public List<StatisticsDto> jobStatistics(Long schoolId, int graduationYear) {
        StringPath aliasValue = Expressions.stringPath("value");

        return jpaQueryFactory.select(Projections.bean(StatisticsDto.class,
                        account.myJob.name.as("title"),
                        account.count().as("value")
                ))
                .from(account)
                .where(
                        schoolIdEq(schoolId),
                        graduationYearEq(graduationYear),
                        account.graduationYear.ne(0)
                )
                .groupBy(account.graduationYear, account.school, account.myJob)
                .orderBy(aliasValue.desc())
                .fetch();
    }

    public List<StatisticsDto> mbtiStatistics(Long schoolId, int graduationYear) {
        StringPath aliasValue = Expressions.stringPath("value");

        return jpaQueryFactory.select(Projections.bean(StatisticsDto.class,
                        account.mbti.name.as("title"),
                        account.count().as("value")
                ))
                .from(account)
                .where(
                        schoolIdEq(schoolId),
                        graduationYearEq(graduationYear),
                        account.graduationYear.ne(0)
                )
                .groupBy(account.graduationYear, account.school, account.mbti)
                .orderBy(aliasValue.desc())
                .fetch();
    }

    private Predicate graduationYearEq(Integer graduationYear) {
        return graduationYear != null ? account.graduationYear.eq(graduationYear) : null;
    }

    private Predicate schoolIdEq(Long schoolId) {
        return schoolId != null ? account.school.id.eq(schoolId) : null;
    }


}
