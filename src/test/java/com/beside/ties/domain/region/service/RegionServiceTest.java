package com.beside.ties.domain.region.service;

import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.region.repo.RegionRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("지역 서비스 로직 테스트")
@ActiveProfiles("test")
@SpringBootTest
class RegionServiceTest {

    @Autowired
    private RegionRepo regionRepo;

    @DisplayName("서비스 state, city 저장 로직 과 조회 로직 잘되는지 확인")
    @Test
    void testParentAndChild() {
        String parent = "경상남도";
        String child = "창원시";

        Region regionParent = new Region(parent);
        Region saveParent = regionRepo.save(regionParent);

        Region regionChild = new Region(child, regionParent);
        Region saveChild = regionRepo.save(regionChild);

        assertEquals(parent, saveParent.getName());
        assertEquals(child, saveChild.getName());
        assertEquals(regionChild.getParent(), regionParent);

        List<Region> actualParentRegions = List.of(regionParent);
        List<Region> actualChildRegions = List.of(regionChild);

        List<Region> parentRegions = regionRepo.findRegionsByParentIsNull();
        List<Region> childRegions = regionRepo.findRegionsByParent(regionParent);

        assertEquals(actualParentRegions.get(0).getName(), parentRegions.get(0).getName());
        assertEquals(actualChildRegions.get(0).getName(), childRegions.get(0).getName());
    }

}