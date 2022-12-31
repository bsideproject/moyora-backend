package com.beside.ties.domain.region.api;

import com.beside.ties.domain.region.dto.RegionResponseDto;
import com.beside.ties.domain.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/region")
@RestController
public class RegionApi {

    private final RegionService regionService;

    @GetMapping("/state")
    public ResponseEntity<List<RegionResponseDto>> findAllStates(){
        List<RegionResponseDto> allParentsRegion = regionService.findAllStates();
        return ResponseEntity.ok().body(allParentsRegion);
    }

    @GetMapping("/city")
    public ResponseEntity<List<RegionResponseDto>> findCitiesByState(
            @RequestParam String state
    ){
        List<RegionResponseDto> allParentsRegion = regionService.findAllCities(state);
        return ResponseEntity.ok().body(allParentsRegion);
    }

    @PostMapping("/state")
    public ResponseEntity<String> saveState(
            @RequestParam String state
    ){
        Long parentId = regionService.saveState(state);
        String message = "state Id is" + parentId;
        return ResponseEntity.ok().body(message);
    }

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
