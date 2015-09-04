package com.semkagtn.lastfm.utils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by semkagtn on 04.09.15.
 */
public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtils() {

    }

    public static <T> T fromJson(JsonNode json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            return null;
        }
    }
}
