package com.beside.ties.domain.region.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@ApiModel
@Getter
public class RegionStateRequestDto {
    @ApiModelProperty(
            example = "경상남도"
    )
    List<String> states;
}
