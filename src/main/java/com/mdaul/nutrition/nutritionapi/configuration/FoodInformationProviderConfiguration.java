package com.mdaul.nutrition.nutritionapi.configuration;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Setter
@Configuration
@ConfigurationProperties(prefix = "food-information-provider")
public class FoodInformationProviderConfiguration {

    public static final String NUTRITION_FACTS_ENDPOINT_NAME = "nutrition-facts";
    private String baseUrl;
    private Map<String, String> endpoints;

    public String getNutritionFactsUrl(long barcode) {
        return String.format("%s/%s",
                getEndpointUrl(NUTRITION_FACTS_ENDPOINT_NAME),
                barcode);
    }

    private String getEndpointUrl(String endpoint) {
        return String.format("%s/%s",
                baseUrl,
                endpoints.get(endpoint));
    }
}
