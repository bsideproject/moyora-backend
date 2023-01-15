package com.beside.ties.domain.region.api;

import com.beside.ties.domain.region.dto.request.RegionCityRequestDto;
import com.beside.ties.domain.region.dto.request.RegionStateRequestDto;
import com.beside.ties.domain.region.dto.response.RegionResponseDto;
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

    @Operation(summary = "여러개의 도, 시 등록")
    @PostMapping("/state")
    public ResponseEntity<String> saveState(
            @RequestBody RegionStateRequestDto requestDto
            ){
        String message = regionService.saveStates(requestDto);
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "여러개 구 등록")
    @PostMapping("/city")
    public ResponseEntity<String> saveCity(
            @RequestBody RegionCityRequestDto requestDto
    ){
        String message = regionService.saveCities(requestDto);
        return ResponseEntity.ok().body(message);
    }

}
