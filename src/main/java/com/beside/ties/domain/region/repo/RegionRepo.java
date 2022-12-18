package com.beside.ties.domain.region.repo;

import com.beside.ties.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RegionRepo extends JpaRepository<Region, Long> {

    public Optional<Region> findRegionByName(String name);

    public List<Region> findRegionsByParentIsNull();

    public List<Region> findRegionsByParent(Region region);
}
