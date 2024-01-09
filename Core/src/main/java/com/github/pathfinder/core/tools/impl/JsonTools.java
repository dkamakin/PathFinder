package com.github.pathfinder.core.tools.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pathfinder.core.exception.DeserializeException;
import com.github.pathfinder.core.exception.SerializeException;
import com.github.pathfinder.core.interfaces.IThrowingFunction;
import java.io.IOException;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JsonTools {

    private final ObjectMapper mapper;

    public void serialize(OutputStream stream, Object object) {
        try {
            mapper.writeValue(stream, object);
        } catch (IOException e) {
            log.error("Failed to serialize {}", object, e);
            throw new SerializeException(e);
        }
    }

    public String serialize(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize {}", object, e);
            throw new SerializeException(e);
        }
    }

    public <T> T deserialize(String json, Class<T> expectedClass) {
        return deserialize(objectMapper -> objectMapper.readValue(json, expectedClass));
    }

    public <T> T deserialize(String json, TypeReference<T> typeReference) {
        return deserialize(objectMapper -> objectMapper.readValue(json, typeReference));
    }

    private <T> T deserialize(IThrowingFunction<ObjectMapper, T, JsonProcessingException> function) {
        try {
            return function.apply(mapper);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize", e);
            throw new DeserializeException(e);
        }
    }

}
