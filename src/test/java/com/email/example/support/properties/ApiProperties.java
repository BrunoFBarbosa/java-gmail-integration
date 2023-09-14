package com.email.example.support.properties;

import com.email.example.support.properties.PropertiesProvider;
import com.email.example.support.properties.PropertiesProviderFactory;

public enum ApiProperties {

    GOOGLE_API_URI("GoogleApiUri"),
    CLIENTE_GOOGLE_ID("ClientGoogleId"),
    CLIENTE_GOOGLE_SECRET("ClientGoogleSecret"),
    CLIENTE_GOOGLE_REFRESH_TOKEN("ClientGoogleRefreshToken");

    private static final PropertiesProvider properties = PropertiesProviderFactory.getJavaPropertiesProvider();

    private final String value;

    ApiProperties(String value) {
        this.value = value;
    }

    public String value() {
        return properties.getProperty(this.value);
    }

}
