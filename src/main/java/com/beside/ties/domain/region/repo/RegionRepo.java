package com.beside.ties.domain.region.repo;

import com.beside.ties.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepo extends JpaRepository<Region, Long> {

    public Optional<Region> findByName(String name);

    public List<Region> findByParentIsNullOrderByPriorityAsc();

    public List<Region> findByParentOrderByNameAsc(Region region);
}
