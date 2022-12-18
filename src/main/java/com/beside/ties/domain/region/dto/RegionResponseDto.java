package com.beside.ties.domain.region.dto;

import com.beside.ties.domain.region.entity.Region;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class RegionResponseDto {
    public String regionName;

    public RegionResponseDto(Region region) {
        this.regionName = region.getName();
    }
}
