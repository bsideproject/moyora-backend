package com.beside.ties.domain.region.dto.response;

import com.beside.ties.domain.region.entity.Region;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class RegionResponseDto {
    public String regionName;

    public Long id;

    public RegionResponseDto(Region region) {

        String regionName = region.getName();
        if(region.name.contains("_")){
            String[] strings = region.name.split("_");
            regionName = strings[1];
        }
        this.regionName = regionName;
    }

    public static RegionResponseDto toDto(Region region){
        return new RegionResponseDto(region.getName(), region.getId());
    }
}
