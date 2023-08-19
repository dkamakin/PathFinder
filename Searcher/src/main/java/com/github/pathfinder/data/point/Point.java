package com.github.pathfinder.data.point;

import com.github.pathfinder.database.entity.LandType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Point {

    private final Integer  altitude;
    private final Integer  longitude;
    private final Integer  latitude;
    private final LandType landType;

}
