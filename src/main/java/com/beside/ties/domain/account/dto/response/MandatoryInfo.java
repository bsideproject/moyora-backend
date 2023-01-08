package com.beside.ties.domain.account.dto.response;

import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.school.entity.School;
import lombok.Getter;

@Getter
public class MandatoryInfo {
    private School school;
    private Region region;
}
