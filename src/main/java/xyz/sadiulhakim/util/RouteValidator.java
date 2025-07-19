package xyz.sadiulhakim.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import xyz.sadiulhakim.pojo.Route;

import java.util.Set;

public class RouteValidator {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void validateRoute(Route route) {
        if (route == null) throw new IllegalArgumentException("Route cannot be null");
        if (route.getPath() == null || route.getPath().isBlank()) {
            throw new IllegalArgumentException("Path is required");
        }

        validatePath(route.getPath());

        String method = route.getMethod() != null ? route.getMethod().toUpperCase() : "GET";

        // Validate body for POST/PUT/PATCH
        if (Set.of("POST", "PUT", "PATCH").contains(method)) {
            if (route.getBody() == null) {
                throw new IllegalArgumentException("Body is required for method " + method);
            }
            validateJsonBody(route.getBody());
        } else {
            if (route.getBody() != null) {
                throw new IllegalArgumentException("Body must be null for method " + method);
            }
        }
    }

    private static void validatePath(String path) {
        if (path.contains("..")) throw new IllegalArgumentException("Path traversal detected");
        if (!path.matches("^(/[a-zA-Z0-9._-]+)+$")) {
            throw new IllegalArgumentException("Invalid characters in path");
        }
    }

    private static void validateJsonBody(Object body) {
        try {
            // Attempt to serialize and deserialize
            String json = mapper.writeValueAsString(body);
            mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON body", e);
        }
    }
}
