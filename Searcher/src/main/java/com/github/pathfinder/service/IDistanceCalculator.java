package com.github.pathfinder.service;

import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.distance.IDistance;

public interface IDistanceCalculator {

    IDistance distance(Coordinate source, Coordinate target);

}
