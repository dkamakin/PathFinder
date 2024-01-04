package com.github.pathfinder.database.mapper;

import com.github.pathfinder.searcher.api.exception.AnnotationNotFoundException;
import com.github.pathfinder.searcher.api.exception.ValueNotFoundException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.MapAccessor;
import org.neo4j.driver.types.MapAccessorWithDefaultValue;
import org.neo4j.driver.types.TypeSystem;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.stereotype.Component;
import static com.github.pathfinder.core.tools.impl.ReflectionTools.cast;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValueMapper {

    private final Neo4jMappingContext neo4jMappingContext;

    @SneakyThrows
    public <T> T map(TypeSystem typeSystem, Class<T> expected, MapAccessorWithDefaultValue fetched) {
        var fields = expected.getDeclaredFields();
        var values = Arrays.stream(fields).map(field -> instantiate(typeSystem, field, fetched)).toArray();
        var types  = Arrays.stream(fields).map(Field::getType).toArray(Class[]::new);

        return expected.getDeclaredConstructor(types).newInstance(values);
    }

    public Object instantiate(TypeSystem typeSystem, Field field, MapAccessorWithDefaultValue fetched) {
        return ValueMappers
                .mapper(field.getType())
                .map(mapper -> mapper.apply(get(fetched, field)))
                .orElseGet(() -> cast(mapComplexField(typeSystem, field, fetched)));
    }

    private <T> T mapComplexField(TypeSystem typeSystem, Field field, MapAccessorWithDefaultValue fetched) {
        var type = field.getType();

        if (Collection.class.isAssignableFrom(type)) {
            return mapCollection(typeSystem, field, fetched);
        } else {
            return cast(mapComplexObject(typeSystem, field.getType(), fetched.get(field.getName())));
        }
    }

    private <T> T mapCollection(TypeSystem typeSystem, Field field, MapAccessorWithDefaultValue fetched) {
        var mapper = collectiomMapper(typeSystem, genericType(field));

        return cast(get(fetched, field).asList(mapper));
    }

    private <T> Mapper<T> collectiomMapper(TypeSystem typeSystem, Class<T> collectionClass) {
        return ValueMappers
                .mapper(collectionClass)
                .orElse(value -> cast(mapComplexObject(typeSystem, collectionClass, value)));
    }

    private Class<?> genericType(Field field) {
        var annotation = field.getAnnotation(CollectionType.class);

        if (annotation == null) {
            throw new AnnotationNotFoundException(CollectionType.class, field);
        } else {
            return annotation.value();
        }
    }

    private <T> T mapComplexObject(TypeSystem typeSystem, Class<T> collectionClass, Value value) {
        return cast(neo4jMapper(collectionClass)
                            .map(mapper -> mapper.apply(typeSystem, value))
                            .orElseGet(() -> map(typeSystem, collectionClass, value)));
    }

    private <T> Optional<BiFunction<TypeSystem, MapAccessor, T>> neo4jMapper(Class<T> aClass) {
        try {
            return Optional.of(neo4jMappingContext.getRequiredMappingFunctionFor(aClass));
        } catch (Exception e) {
            log.debug("Mapper for {} not found: {}", aClass, e.getMessage());
            return Optional.empty();
        }
    }

    private Value get(MapAccessorWithDefaultValue fetched, Field field) {
        var name = field.getName();

        try {
            var value = fetched.get(name);

            if (value.isNull()) {
                throw new ValueNotFoundException(name);
            }

            return value;
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Failed to get a field", e);
            throw new ValueNotFoundException(name);
        }
    }

}
