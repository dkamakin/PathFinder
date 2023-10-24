package com.github.pathfinder.database.mapper;

import com.github.pathfinder.core.tools.impl.ReflectionTools;
import java.util.Map;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.neo4j.driver.Value;

@UtilityClass
public class ValueMappers {

    private static final Map<Class<?>, Mapper<?>> MAPPERS = Map.of(
            Double.class, Value::asDouble,
            Float.class, Value::asFloat,
            String.class, Value::asString,
            Boolean.class, Value::asBoolean,
            byte[].class, Value::asByteArray,
            Long.class, Value::asLong,
            Integer.class, Value::asInt
    );

    private static final Map<Class<?>, Mapper<?>> PRIMITIVE_MAPPERS = Map.of(
            double.class, Value::asDouble,
            float.class, Value::asFloat,
            boolean.class, Value::asBoolean,
            byte[].class, Value::asByteArray,
            long.class, Value::asLong,
            int.class, Value::asInt
    );

    public <T> Optional<Mapper<T>> mapper(Class<T> aClass) {
        var map = aClass.isPrimitive() ? PRIMITIVE_MAPPERS : MAPPERS;

        return ReflectionTools.cast(Optional.ofNullable(map.get(aClass)));
    }

}
