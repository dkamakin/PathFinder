package com.github.pathfinder.core.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pathfinder.core.exception.DeserializeException;
import com.github.pathfinder.core.exception.SerializeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JsonTools {

    private final ObjectMapper mapper;

    public String serialize(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize {}", object, e);
            throw new SerializeException(e);
        }
    }

    public <T> T deserialize(String json, Class<T> expectedClass) {
        try {
            return mapper.readValue(json, expectedClass);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize {}", json, e);
            throw new DeserializeException(e);
        }
    }

}
