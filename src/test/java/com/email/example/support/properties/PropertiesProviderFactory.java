package com.email.example.support.properties;

public class PropertiesProviderFactory {

    private PropertiesProviderFactory() {
    }

    public static PropertiesProvider getJavaPropertiesProvider() {
        return new JavaPropertiesProvider();
    }


}
