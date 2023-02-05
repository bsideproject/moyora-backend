package com.beside.ties.domain.schoolmbti.entity;

import com.beside.ties.domain.mbti.entity.Mbti;
import com.beside.ties.domain.school.entity.School;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class SchoolMbti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_mbti_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mbti_id")
    private Mbti mbti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @Column(name = "count")
    private Long count;

    public SchoolMbti(Mbti mbti, School school, Long count) {
        this.mbti = mbti;
        this.school = school;
        this.count = count;
    }

    public SchoolMbti(Mbti mbti, School school) {
        this.mbti = mbti;
        this.school = school;
        this.count = 1L;
    }

    public void plusOneCount() {
        this.count = this.count + 1L;
    }

    public void minusOneCount() {
        this.count = this.count - 1L;
    }

    public static SchoolMbti createSchoolMbti(Mbti mbti, School school, Long count) {
        return new SchoolMbti(mbti, school, count);
    }
}

