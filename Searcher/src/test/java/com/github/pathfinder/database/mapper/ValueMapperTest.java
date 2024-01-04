package com.github.pathfinder.database.mapper;

import com.github.pathfinder.searcher.api.exception.AnnotationNotFoundException;
import com.github.pathfinder.searcher.api.exception.ValueNotFoundException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.InternalRecord;
import org.neo4j.driver.internal.types.InternalTypeSystem;
import org.neo4j.driver.internal.value.FloatValue;
import org.neo4j.driver.internal.value.ListValue;
import org.neo4j.driver.internal.value.StringValue;
import org.neo4j.driver.types.MapAccessor;
import org.neo4j.driver.types.MapAccessorWithDefaultValue;
import org.neo4j.driver.types.TypeSystem;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.data.neo4j.core.mapping.UnknownEntityException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValueMapperTest {

    @Mock
    Neo4jMappingContext neo4jMappingContext;

    @InjectMocks
    ValueMapper valueMapper;

    List<String> generateKeys(int size) {
        return IntStream.range(0, size).boxed().map(Object::toString).toList();
    }

    Value mockValue(Consumer<Value> modifier) {
        var mock = mock(Value.class);

        modifier.accept(mock);

        return mock;
    }

    Value valueAdapter(String childName, Consumer<Value> childModifier) {
        var child  = mockValue(childModifier);
        var parent = mock(Value.class);

        when(parent.get(childName)).thenReturn(child);

        return parent;
    }

    Value listValue(Value... values) {
        return new ListValue(values);
    }

    void whenNeedNeo4jMapperNotNeeded() {
        when(neo4jMappingContext.getRequiredMappingFunctionFor(any())).thenThrow(UnknownEntityException.class);
    }

    <T> void whenNeo4jMapperNeeded(Class<T> aClass, BiFunction<TypeSystem, MapAccessor, T> mapper) {
        when(neo4jMappingContext.getRequiredMappingFunctionFor(aClass)).thenReturn(mapper);
    }

    <T> BiFunction<TypeSystem, MapAccessor, T> neo4Mapper(T expected) {
        var result = mock(BiFunction.class);

        when(result.apply(any(), any())).thenReturn(expected);

        return result;
    }

    @Test
    void map_TargetHasNoFields_ReturnTarget() {
        var expected = new NoFields();
        var values   = new Value[]{new FloatValue(1D), new StringValue("as")};
        var fetched  = new InternalRecord(generateKeys(values.length), values);
        var actual   = valueMapper.map(InternalTypeSystem.TYPE_SYSTEM, expected.getClass(), fetched);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void map_TargetHasOnlyPrimitives_ReturnTarget() {
        var expected = new PrimitiveFields(1D, 1F, 1L, 1);
        var values = new Value[]{
                mockValue(value -> when(value.asDouble()).thenReturn(expected.aDouble())),
                mockValue(value -> when(value.asFloat()).thenReturn(expected.aFloat())),
                mockValue(value -> when(value.asLong()).thenReturn(expected.aLong())),
                mockValue(value -> when(value.asInt()).thenReturn(expected.aInt()))
        };
        var keys = List.of(
                "aDouble", "aFloat", "aLong", "aInt"
        );
        var fetched = new InternalRecord(keys, values);
        var actual  = valueMapper.map(InternalTypeSystem.TYPE_SYSTEM, expected.getClass(), fetched);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void map_TargetContainsObjectFields_ReturnTarget() {
        var expected = new ObjectFields(1D, 1F, 1L, 1, new byte[]{1, 2, 3});
        var values = new Value[]{
                mockValue(value -> when(value.asDouble()).thenReturn(expected.aDouble())),
                mockValue(value -> when(value.asFloat()).thenReturn(expected.aFloat())),
                mockValue(value -> when(value.asLong()).thenReturn(expected.aLong())),
                mockValue(value -> when(value.asInt()).thenReturn(expected.aInt())),
                mockValue(value -> when(value.asByteArray()).thenReturn(expected.byteArray()))
        };
        var keys = List.of(
                "aDouble", "aFloat", "aLong", "aInt", "byteArray"
        );
        var fetched = new InternalRecord(keys, values);
        var actual  = valueMapper.map(InternalTypeSystem.TYPE_SYSTEM, expected.getClass(), fetched);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void map_TargetContainsCollectionNotNeo4jEntity_ReturnTarget() {
        var aLong = 1L;
        var expected = new CollectionFields(1D, 1, List.of(1F), List.of("test"),
                                            List.of(new ObjectField(aLong)));
        var values = new Value[]{
                mockValue(value -> when(value.asDouble()).thenReturn(expected.aDouble())),
                mockValue(value -> when(value.asInt()).thenReturn(expected.aInt())),
                listValue(mockValue(value -> when(value.asFloat()).thenReturn(expected.floatList().get(0)))),
                listValue(mockValue(value -> when(value.asString()).thenReturn(expected.stringList().get(0)))),
                listValue(valueAdapter("aLong", value -> when(value.asLong()).thenReturn(aLong)))
        };
        var keys = List.of(
                "aDouble", "aInt", "floatList", "stringList", "objectList"
        );
        var fetched = new InternalRecord(keys, values);

        whenNeedNeo4jMapperNotNeeded();

        var actual = valueMapper.map(InternalTypeSystem.TYPE_SYSTEM, expected.getClass(), fetched);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void map_TargetContainsNeo4jEntityList_ReturnTarget() {
        var aLong    = 1L;
        var entity   = mock(Neo4jEntity.class);
        var expected = new Neo4jEntitiesCollection(List.of(entity), List.of(new ObjectField(aLong)), 12);
        var values = new Value[]{
                listValue(mock(Value.class)),
                listValue(valueAdapter("aLong", value -> when(value.asLong()).thenReturn(aLong))),
                mockValue(value -> when(value.asInt()).thenReturn(expected.aInt())),
        };
        var keys    = List.of("entityList", "objectList", "aInt");
        var fetched = new InternalRecord(keys, values);

        whenNeo4jMapperNeeded(Neo4jEntity.class, neo4Mapper(entity));

        var actual = valueMapper.map(InternalTypeSystem.TYPE_SYSTEM, expected.getClass(), fetched);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void map_TargetHasNotAnnotatedCollection_AnnotationNotFoundException() {
        var expected      = new NotAnnotated(List.of());
        var fetched       = mock(MapAccessorWithDefaultValue.class);
        var expectedClass = expected.getClass();

        assertThatThrownBy(() -> valueMapper.map(InternalTypeSystem.TYPE_SYSTEM, expectedClass, fetched))
                .isInstanceOf(AnnotationNotFoundException.class);
    }

    @Test
    void map_FieldNotFoundInResponse_ValueNotFoundException() {
        var expected      = new ObjectFields(1D, 1F, 1L, 1, new byte[]{1, 2, 3});
        var expectedClass = expected.getClass();
        var values = new Value[]{
        };
        var keys = List.of(
                "aDouble", "aFloat", "aLong", "aInt", "byteArray"
        );
        var fetched = new InternalRecord(keys, values);

        assertThatThrownBy(() -> valueMapper.map(InternalTypeSystem.TYPE_SYSTEM, expectedClass, fetched))
                .isInstanceOf(ValueNotFoundException.class);
    }

    public record NoFields() {

    }

    public record PrimitiveFields(double aDouble,
                                  float aFloat,
                                  long aLong,
                                  int aInt) {

    }

    public record ObjectFields(Double aDouble,
                               Float aFloat,
                               Long aLong,
                               Integer aInt,
                               byte[] byteArray) {

    }

    public record ObjectField(long aLong) {

    }

    public record CollectionFields(Double aDouble,
                                   int aInt,
                                   @CollectionType(Float.class)
                                   List<Float> floatList,
                                   @CollectionType(String.class)
                                   List<String> stringList,
                                   @CollectionType(ObjectField.class)
                                   List<ObjectField> objectList) {

    }

    public record Neo4jEntity(String name) {

    }

    public record Neo4jEntitiesCollection(@CollectionType(Neo4jEntity.class) List<Neo4jEntity> entityList,
                                          @CollectionType(ObjectField.class) List<ObjectField> objectList,
                                          int aInt) {

    }

    public record NotAnnotated(List<Neo4jEntity> entityList) {

    }
}
