package com.email.example.support.properties;

import java.io.InputStream;
import java.util.Properties;

public class JavaPropertiesProvider implements PropertiesProvider {

    private final Properties properties;

    public JavaPropertiesProvider() {
        this.properties = loadPropertiesFile();
    }

    private String getFilename() {
        return "/api.properties";
    }

    private Properties loadPropertiesFile() {
        String file = getFilename();
        try (InputStream input = JavaPropertiesProvider.class.getResourceAsStream(file)) {
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Could not load properties file at %s. Please verify that the file exists.", file));
        }
    }

    @Override
    public String getProperty(String propertyName) {
        String property = properties.getProperty(propertyName);
        if (property == null) {
            throw new IllegalArgumentException(String.format("Property %s does not exist in properties file.", propertyName));
        }
        return property;
    }

}
