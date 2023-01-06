package com.beside.ties.domain.school.dto;

import com.beside.ties.domain.school.entity.School;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class SchoolDto {
    private Long id;
    private String schoolName;
    private String establishmentDate;
    private String address;
    private String schoolCode;

    public SchoolDto(School school) {
        this.id = school.getId();
        this.schoolName = school.getSchoolName();
        this.establishmentDate = school.getEstablishmentDate();
        this.address = school.getAddress();
        this.schoolCode = school.getSchoolCode();
    }

    public static School toSchool(SchoolDto schoolDto) {
        return new School(
                schoolDto.getSchoolName(),
                schoolDto.getEstablishmentDate(),
                schoolDto.getAddress(),
                schoolDto.getSchoolCode());
    }
}
