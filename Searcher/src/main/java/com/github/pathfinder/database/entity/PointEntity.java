package com.github.pathfinder.database.entity;

import com.google.common.base.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Node(PointEntity.Token.NODE_NAME)
public class PointEntity {

    @UtilityClass
    public static class Token {

        public static final String NODE_NAME  = "Point";
        public static final String CONNECTION = "CONNECTION";
        public static final String ALTITUDE   = "altitude";
        public static final String LONGITUDE  = "longitude";
        public static final String LATITUDE   = "latitude";
        public static final String LAND_TYPE  = "landType";
    }

    @Id
    @GeneratedValue
    private String id;

    @ToString.Exclude
    @Relationship(type = Token.CONNECTION)
    private Set<PointEntity> connection;

    @Property(Token.ALTITUDE)
    private Double altitude;

    @Property(Token.LONGITUDE)
    private Double longitude;

    @Property(Token.LATITUDE)
    private Double latitude;

    @Property(Token.LAND_TYPE)
    private LandType landType;

    public PointEntity(Double altitude, Double longitude, Double latitude, LandType landType) {
        this.altitude  = altitude;
        this.longitude = longitude;
        this.latitude  = latitude;
        this.landType  = landType;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PointEntity that = (PointEntity) object;
        return Objects.equal(altitude, that.altitude) &&
                Objects.equal(longitude, that.longitude) &&
                Objects.equal(latitude, that.latitude) &&
                landType == that.landType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(altitude, longitude, latitude, landType);
    }
}
