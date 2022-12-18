package com.beside.ties.domain.region.service;

import com.beside.ties.domain.region.dto.RegionResponseDto;
import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.region.repo.RegionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class RegionService {

    private final RegionRepo regionRepo;

    public Long saveState(String name){
        Region region = new Region(name);
        Region save = regionRepo.save(region);
        return save.getId();
    }

    public Long saveCity(String parentName, String name){

        Optional<Region> parentOptional = regionRepo.findRegionByName(parentName);

        if(parentOptional.isEmpty()){
            throw new IllegalArgumentException("부모 지역 이름이 존재하지 않습니다.");
        }
        Region parent = parentOptional.get();

        Region region = new Region(name, parent);
        Region save = regionRepo.save(region);
        return save.getId();
    }

    public List<RegionResponseDto> findAllStates(){
        List<Region> regionsByParentIsNull = regionRepo.findRegionsByParentIsNull();
        List<RegionResponseDto> collect = regionsByParentIsNull.stream().map(RegionResponseDto::new).collect(Collectors.toList());
        return collect;
    }

    public List<RegionResponseDto> findAllCities(String name){
        Optional<Region> parentRegionOptional = regionRepo.findRegionByName(name);
        if(parentRegionOptional.isEmpty()){
            throw new IllegalArgumentException("없는 도/시 입니다.");
        }
        List<Region> regionsByParentIsNull = regionRepo.findRegionsByParent(parentRegionOptional.get());
        List<RegionResponseDto> collect = regionsByParentIsNull.stream().map(RegionResponseDto::new).collect(Collectors.toList());
        return collect;
    }
}
