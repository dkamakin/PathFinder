package com.github.pathfinder.data.point;

import com.github.pathfinder.database.entity.LandType;

public record Point(Integer altitude,
                    Integer longitude,
                    Integer latitude,
                    LandType landType) {

}
