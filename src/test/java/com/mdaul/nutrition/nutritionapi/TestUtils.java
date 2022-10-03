package com.mdaul.nutrition.nutritionapi;

import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.testcontainers.shaded.com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class TestUtils {

    public HttpHeaders getTokenizedHeader(String token) {
        return new HttpHeaders(
                CollectionUtils.toMultiValueMap(
                        Map.of("Authorization",
                                List.of("Bearer " + token))));
    }

    public String getResource(String resource) throws IOException {
        URL url = Resources.getResource(resource);
        return Resources.toString(url, StandardCharsets.UTF_8);
    }
}
