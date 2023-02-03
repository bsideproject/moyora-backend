package com.beside.ties.domain.region.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Parent;

import javax.persistence.*;
import java.util.Set;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Region {

    public Region(
            String name
    ){
        this.name = name;
    }

    public Region(
            String name,
            Region parent
    ){
        this.name = name;
        this.parent = parent;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    Long id;

    @Column(length = 50, unique = true)
    public String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    public Region parent;

    public int priority;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    Set<Region> children;


}
