package com.github.pathfinder.database.entity;

import com.google.common.base.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@ToString
@NoArgsConstructor
@Node(PointEntity.Token.NODE_NAME)
public class PointEntity {

    @UtilityClass
    public static class Token {

        public static final String NODE_NAME  = "Point";
        public static final String CONNECTION = "CONNECTION";
    }

    @Id
    @GeneratedValue
    private Long id;

    @ToString.Exclude
    @Relationship(type = Token.CONNECTION)
    private Set<PointEntity> connection;

    private Double   altitude;
    private Double   longitude;
    private Double   latitude;
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
        return Objects.equal(id, that.id) &&
                Objects.equal(altitude, that.altitude) &&
                Objects.equal(longitude, that.longitude) &&
                Objects.equal(latitude, that.latitude) &&
                landType == that.landType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, altitude, longitude, latitude, landType);
    }
}
