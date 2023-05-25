package ru.gobar.classifier.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class RequestViolator {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static ObjectNode replace(String key, Object target, Object value) {
        ObjectNode result = objectMapper.valueToTree(target);
        result.replace(key, objectMapper.valueToTree(value));
        return result;
    }

    public static ObjectNode replace(String key, String path, Object target, Object value) {
        ObjectNode result = objectMapper.valueToTree(target);
        ((ObjectNode) result.at(path)).replace(key, objectMapper.valueToTree(value));
        return result;
    }

    public static ObjectNode remove(String key, Object target) {
        ObjectNode result = objectMapper.valueToTree(target);
        result.remove(key);
        return result;
    }

    public static ObjectNode remove(String key, String path, Object target) {
        ObjectNode result = objectMapper.valueToTree(target);
        ((ObjectNode) result.at(path)).remove(key);
        return result;
    }
}
