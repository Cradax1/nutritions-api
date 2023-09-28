package com.mdaul.nutrition.nutritionapi;

import org.testcontainers.shaded.com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TestUtils {

    public String getResource(String resource) throws IOException {
        URL url = Resources.getResource(resource);
        return Resources.toString(url, StandardCharsets.UTF_8);
    }
}
