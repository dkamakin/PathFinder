package com.github.pathfinder.data.point;

import com.github.pathfinder.database.entity.LandType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class IndexedPoint extends Point {

    private final Long id;

    public IndexedPoint(Integer altitude, Integer longitude, Integer latitude, LandType landType, Long id) {
        super(altitude, longitude, latitude, landType);

        this.id = id;
    }
}
