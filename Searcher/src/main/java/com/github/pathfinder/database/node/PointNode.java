package com.github.pathfinder.database.node;

import com.github.pathfinder.core.data.Coordinate;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.types.GeographicPoint2d;
import org.springframework.data.neo4j.types.GeographicPoint3d;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Node(PointNode.Token.NODE_NAME)
public class PointNode {

    @UtilityClass
    public static class Token {

        public static final String NODE_NAME               = "Point";
        public static final String ID                      = "id";
        public static final String ALTITUDE                = "altitude";
        public static final String LONGITUDE               = "longitude";
        public static final String LATITUDE                = "latitude";
        public static final String LAND_TYPE               = "landType";
        public static final String LOCATION_2D             = "location2d";
        public static final String LOCATION_3D             = "location3d";
        public static final String PASSABILITY_COEFFICIENT = "passabilityCoefficient";
    }

    @Id
    @GeneratedValue
    private String internalId;

    @NotNull
    @Property(Token.ID)
    private UUID id;

    @Relationship(type = PointRelation.Token.TYPE)
    private Set<PointRelation> relations;

    @NotNull
    @Property(Token.LOCATION_3D)
    private GeographicPoint3d location3d;

    @NotNull
    @Property(Token.LOCATION_2D)
    private GeographicPoint2d location2d;

    @Property(Token.ALTITUDE)
    private double altitude;

    @Property(Token.LATITUDE)
    @DecimalMin(Coordinate.Constraint.LATITUDE_MIN_VALUE_STRING)
    @DecimalMax(Coordinate.Constraint.LATITUDE_MAX_VALUE_STRING)
    private double latitude;

    @Property(Token.LONGITUDE)
    @DecimalMin(Coordinate.Constraint.LONGITUDE_MIN_VALUE_STRING)
    @DecimalMax(Coordinate.Constraint.LONGITUDE_MAX_VALUE_STRING)
    private double longitude;

    @NotBlank
    @Property(Token.LAND_TYPE)
    private String landType;

    @Property(Token.PASSABILITY_COEFFICIENT)
    private double passabilityCoefficient;

    protected PointNode(UUID id, Set<PointRelation> relations, GeographicPoint3d location, String landType,
                        Double passabilityCoefficient) {
        this.id                     = id;
        this.relations              = relations;
        this.location3d             = location;
        this.location2d             = new GeographicPoint2d(location.getLatitude(), location.getLongitude());
        this.landType               = landType;
        this.altitude               = location.getHeight();
        this.latitude               = location.getLatitude();
        this.longitude              = location.getLongitude();
        this.passabilityCoefficient = passabilityCoefficient;
    }

    public static PointNodeBuilder builder() {
        return new PointNodeBuilder();
    }

    public Double latitude() {
        return location2d.getLatitude();
    }

    public Double longitude() {
        return location2d.getLongitude();
    }

    public PointNode add(PointRelation relation) {
        relations.add(relation);
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("internalId", internalId)
                .add("id", id)
                .add("relations", relations.size())
                .add("location3d", location3d)
                .add("location2d", location2d)
                .add("altitude", altitude)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("landType", landType)
                .add("passabilityCoefficient", passabilityCoefficient)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PointNode pointNode = (PointNode) o;
        return Objects.equal(id, pointNode.id) &&
                Objects.equal(altitude, pointNode.altitude) &&
                Objects.equal(location3d, pointNode.location3d) &&
                Objects.equal(location2d, pointNode.location2d) &&
                Objects.equal(latitude, pointNode.latitude) &&
                Objects.equal(longitude, pointNode.longitude) &&
                Objects.equal(landType, pointNode.landType) &&
                Objects.equal(passabilityCoefficient, pointNode.passabilityCoefficient);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, altitude, location3d, location2d, latitude, longitude, landType,
                                passabilityCoefficient);
    }
}
