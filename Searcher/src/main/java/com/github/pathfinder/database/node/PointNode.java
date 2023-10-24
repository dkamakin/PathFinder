package com.github.pathfinder.database.node;

import com.github.pathfinder.core.tools.impl.NullHelper;
import com.google.common.base.Objects;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
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
@Node(PointNode.Token.NODE_NAME)
public class PointNode {

    @UtilityClass
    public static class Token {

        public static final String NODE_NAME  = "Point";
        public static final String CONNECTION = "CONNECTION";
        public static final String ID         = "id";
        public static final String ALTITUDE   = "altitude";
        public static final String LONGITUDE  = "longitude";
        public static final String LATITUDE   = "latitude";
        public static final String LAND_TYPE  = "landType";
    }

    @Id
    @GeneratedValue
    private String internalId;

    @NotNull
    @Property(Token.ID)
    private UUID id;

    @ToString.Exclude
    @Relationship(type = Token.CONNECTION)
    private Set<PointRelation> relations;

    @NotNull
    @Property(Token.ALTITUDE)
    private Double altitude;

    @NotNull
    @Property(Token.LONGITUDE)
    private Double longitude;

    @NotNull
    @Property(Token.LATITUDE)
    private Double latitude;

    @NotNull
    @Property(Token.LAND_TYPE)
    private LandType landType;

    public PointNode() {
        this.id = UUID.randomUUID();
    }

    public PointNode(UUID id, Set<PointRelation> relations, Double altitude, Double longitude, Double latitude,
                     LandType landType) {
        this(null, id, relations, altitude, longitude, latitude, landType);
    }

    public PointNode(String internalId, UUID id, Set<PointRelation> relations, Double altitude, Double longitude,
                     Double latitude, LandType landType) {
        this.internalId = internalId;
        this.id         = NullHelper.notNull(id, UUID::randomUUID);
        this.relations  = relations;
        this.altitude   = altitude;
        this.longitude  = longitude;
        this.latitude   = latitude;
        this.landType   = landType;
    }

    public PointNode(Double altitude, Double longitude, Double latitude, LandType landType) {
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
        PointNode that = (PointNode) object;
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
