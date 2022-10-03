package com.mdaul.nutrition.nutritionapi.model.foodinformationprovider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdaul.nutrition.nutritionapi.exception.FoodInformationProviderItemException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @JsonProperty
    private String brands;
    @JsonProperty
    private Nutriments nutriments;
    @JsonProperty(value = "product_name")
    private String productName;

    @JsonCreator
    public Product(
            @JsonProperty(required = true) String brands,
            @JsonProperty(required = true) Nutriments nutriments,
            @JsonProperty(required = true, value = "product_name") String productName) {
        if (brands == null || nutriments == null || productName == null) {
            throw new FoodInformationProviderItemException(
                    "Cannot build InformationProviderProduct because a required parameter is null");
        }
        this.brands = brands;
        this.nutriments = nutriments;
        this.productName = productName;
    }
}
