package ru.gobar.classifier;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

@UtilityClass
public class PropertyReader {

    private static final Properties PROPERTIES;
    private static final Properties QUERIES;

    static {
        PROPERTIES = readProperties("application.properties");
        QUERIES = readProperties("queries.properties");
    }

    @SneakyThrows
    private static Properties readProperties(String propFile) {
        try (FileInputStream file = new FileInputStream(
                new File(Objects.requireNonNull(
                        PropertyReader.class.getClassLoader().getResource(propFile)).toURI()))) {
            Properties properties = new Properties();
            properties.load(new InputStreamReader(file, StandardCharsets.UTF_8));
            return properties;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String getQuery(String name) {
        return QUERIES.getProperty(name);
    }

    public static String getProperty(String name) {
        return Optional.ofNullable(System.getenv(name)).orElse(PROPERTIES.getProperty(name));
    }
}
