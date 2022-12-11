package com.beside.ties.domain.region;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Region {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    Long id;

    @Column(length = 50, unique = true)
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    Region parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    Set<Region> children;


}
