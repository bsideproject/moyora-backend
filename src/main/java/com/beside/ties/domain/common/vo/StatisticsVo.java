package com.beside.ties.domain.common.vo;

import com.beside.ties.domain.common.dto.StatisticsDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatisticsVo {
    private List<Long> chart;
    private List<StatisticsDto> data;
}
