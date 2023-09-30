package com.github.pathfinder.data.point;

import com.github.pathfinder.database.entity.LandType;

public record Point(Double altitude,
                    Double longitude,
                    Double latitude,
                    LandType landType) {

}
