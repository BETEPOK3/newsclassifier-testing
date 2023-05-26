package ru.gobar.classifier.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.qameta.allure.jsonunit.JsonPatchMatcher;
import net.javacrumbs.jsonunit.core.Option;
import org.opentest4j.AssertionFailedError;

public class AssertUtil {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static void assertEquals(Object expected, Object actual, String... ignorable) {
        String expectedStr = objectMapper.valueToTree(expected).toPrettyString();
        String actualStr = objectMapper.valueToTree(actual).toPrettyString();
        if (!JsonPatchMatcher.jsonEquals(expectedStr)
                .whenIgnoringPaths(ignorable).when(Option.IGNORING_ARRAY_ORDER).matches(actualStr)) {
            throw new AssertionFailedError("Действительное значение не соответствует ожидаемому", expectedStr, actualStr);
        }
    }
}
