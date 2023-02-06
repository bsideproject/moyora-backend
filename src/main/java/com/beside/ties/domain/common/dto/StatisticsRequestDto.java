package com.beside.ties.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticsRequestDto {
    private Long graduationYear;
    private Long schoolId;
}
