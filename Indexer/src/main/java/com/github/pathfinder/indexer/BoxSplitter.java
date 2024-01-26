package com.github.pathfinder.indexer;

import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.searcher.api.data.IndexBox;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.geotools.referencing.GeodeticCalculator;

/**
 * This is a simple algorithm of splitting big bounding boxes into smaller chunks.
 * 1. Calculate current box size, if it's smaller than expected, then return
 * 2. Otherwise, split it into 2 smaller boxes and process recursively p.1.
 */
@Slf4j
public class BoxSplitter {

    static final double             ADDITIONAL_SPACE_FOR_BOX = 50D;
    static final int                METERS_IN_KM             = 1000;
    static final double             SQUARE_KM_LIMIT          = 50D;
    static final GeodeticCalculator CALCULATOR               = new GeodeticCalculator();

    public static void main(String[] args) {
        var result = splitBox(new IndexBox(1,
                                           new Coordinate(45.104546, 19.593430),
                                           new Coordinate(45.314495, 19.963531)));

        System.out.println(IntStream.range(0, result.size()).boxed()
                                   .map(i -> formatToLiquibaseInsert(i + 1, result.get(i)))
                                   .collect(Collectors.joining("\n")));
    }

    private static String formatToLiquibaseInsert(int index, IndexBox box) {
        return """
                  <insert tableName="index_box">
                            <column name="id">%s</column>
                            <column name="CONNECTED">false</column>
                            <column name="SAVED">false</column>
                            <column name="MIN_LATITUDE">%s</column>
                            <column name="MIN_LONGITUDE">%s</column>
                            <column name="MAX_LATITUDE">%s</column>
                            <column name="MAX_LONGITUDE">%s</column>
                        </insert>
                """.formatted(index, box.min().latitude(), box.min().longitude(), box.max().latitude(),
                              box.max().longitude());
    }

    private static List<IndexBox> splitBox(IndexBox box) {
        var widthMeters  = widthMeters(box);
        var heightMeters = heightMeters(box);
        var squareKmArea = squareKm(widthMeters, heightMeters);

        log.info("Splitting a box: {}, width: {} meters, height: {} meters, area: {} km^2", box, widthMeters,
                 heightMeters,
                 squareKmArea);

        if (squareKmArea < SQUARE_KM_LIMIT) {
            log.info("Box size smaller than a limit, returning...");
            return List.of(box);
        }

        log.info("Box size exceeds the limit, splitting...");

        CALCULATOR.setStartingGeographicPoint(box.min().longitude(), box.min().latitude());
        CALCULATOR.setDirection(90, widthMeters / 2 + ADDITIONAL_SPACE_FOR_BOX);

        var halfPoint           = CALCULATOR.getDestinationGeographicPoint();
        var halfPointCoordinate = new Coordinate(halfPoint.getY(), halfPoint.getX());

        log.info("Half point coordinate: {}", halfPointCoordinate);

        var leftBox = splitBox(new IndexBox(box.id() + 1,
                                            box.min(),
                                            new Coordinate(box.max().latitude(), halfPoint.getX())));
        var rightBox = splitBox(new IndexBox(box.id() + 2,
                                             halfPointCoordinate,
                                             box.max()));

        return Stream.concat(leftBox.stream(), rightBox.stream()).toList();
    }

    private static double squareKm(double widthMeters, double heightMeters) {
        return (widthMeters * heightMeters) / (METERS_IN_KM * METERS_IN_KM);
    }

    private static double widthMeters(IndexBox box) {
        CALCULATOR.setStartingGeographicPoint(box.min().longitude(), box.min().latitude());
        CALCULATOR.setDestinationGeographicPoint(box.max().longitude(), box.min().latitude());
        return CALCULATOR.getOrthodromicDistance();
    }

    private static double heightMeters(IndexBox box) {
        CALCULATOR.setStartingGeographicPoint(box.min().longitude(), box.min().latitude());
        CALCULATOR.setDestinationGeographicPoint(box.min().longitude(), box.max().latitude());
        return CALCULATOR.getOrthodromicDistance();
    }

}
