package com.github.pathfinder.indexer.tools;

import lombok.experimental.UtilityClass;
import org.apache.hc.core5.http.HttpStatus;

@UtilityClass
public class HttpTools {

    public boolean isServerError(int statusCode) {
        return statusCode >= HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

}
