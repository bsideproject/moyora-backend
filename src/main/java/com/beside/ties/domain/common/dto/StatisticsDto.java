package com.beside.ties.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDto {
    private String title;
    private Long value;

    public void convertPercent(Long totalCount) {
        this.value = (long)Math.floor((double) this.value / (double) totalCount * 100.0);
    }
}
