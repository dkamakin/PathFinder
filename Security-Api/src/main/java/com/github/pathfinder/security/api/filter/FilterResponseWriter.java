package com.github.pathfinder.security.api.filter;

import com.github.pathfinder.core.tools.impl.JsonTools;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilterResponseWriter {

    private final JsonTools jsonTools;

    public StatusSetter to(HttpServletResponse response) {
        return new StatusSetter(jsonTools, response);
    }

    @RequiredArgsConstructor
    public static class StatusSetter {

        private final JsonTools           jsonTools;
        private final HttpServletResponse response;

        public ResponseWriter status(int status) {
            return new ResponseWriter(jsonTools, response, status);
        }

    }

    @RequiredArgsConstructor
    public static class ResponseWriter {

        private final JsonTools           jsonTools;
        private final HttpServletResponse response;
        private final int                 status;

        public void write(Object object) throws IOException {
            response.setStatus(status);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            jsonTools.serialize(response.getOutputStream(), object);
        }

    }

}
