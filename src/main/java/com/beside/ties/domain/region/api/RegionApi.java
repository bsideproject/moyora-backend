package com.beside.ties.domain.region.api;

import com.beside.ties.domain.region.dto.RegionResponseDto;
import com.beside.ties.domain.region.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "지역 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/region")
@RestController
public class RegionApi {

    private final RegionService regionService;

    @Operation(summary = "도, 시 조회")
    @GetMapping("/state")
    public ResponseEntity<List<RegionResponseDto>> findAllStates(){
        List<RegionResponseDto> allParentsRegion = regionService.findAllStates();
        return ResponseEntity.ok().body(allParentsRegion);
    }

    @Operation(summary = "구 조회")
    @GetMapping("/city")
    public ResponseEntity<List<RegionResponseDto>> findCitiesByState(
            @RequestParam String state
    ){
        List<RegionResponseDto> allParentsRegion = regionService.findAllCities(state);
        return ResponseEntity.ok().body(allParentsRegion);
    }

    @Operation(summary = "도, 시 등록")
    @PostMapping("/state")
    public ResponseEntity<String> saveState(
            @RequestParam String state
    ){
        Long parentId = regionService.saveState(state);
        String message = "state Id is" + parentId;
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "구 등록")
    @PostMapping("/city")
    public ResponseEntity<String> saveCity(
            @RequestParam String city,
            @RequestParam String state
    ){
        Long cityId = regionService.saveCity(state, city);
        String message = "city Id is" + cityId;
        return ResponseEntity.ok().body(message);
    }

}
