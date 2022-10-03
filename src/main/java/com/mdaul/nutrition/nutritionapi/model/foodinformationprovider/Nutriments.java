package com.mdaul.nutrition.nutritionapi.model.foodinformationprovider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdaul.nutrition.nutritionapi.exception.FoodInformationProviderItemException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Getter
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Nutriments {

    @JsonProperty(value = "energy-kcal_100g")
    private Integer calories;
    @JsonProperty(value = "carbohydrates_100g")
    private BigDecimal carbohydrates;
    @JsonProperty(value = "proteins_100g")
    private BigDecimal proteins;
    @JsonProperty(value = "fat_100g")
    private BigDecimal fat;
    @JsonProperty(value = "fiber_100g")
    private BigDecimal fiber;

    @JsonCreator
    public Nutriments(
            @JsonProperty(required = true, value = "energy-kcal_100g") Integer calories,
            @JsonProperty(required = true, value = "carbohydrates_100g") BigDecimal carbohydrates,
            @JsonProperty(required = true, value = "proteins_100g") BigDecimal proteins,
            @JsonProperty(required = true, value = "fat_100g") BigDecimal fat,
            @JsonProperty(required = true, value = "fiber_100g") BigDecimal fiber) {
        if (calories == null || carbohydrates == null || proteins == null || fat == null || fiber == null) {
            throw new FoodInformationProviderItemException(
                    "Cannot build InformationProviderProduct because a required parameter is null");
        }
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.fat = fat;
        this.fiber = fiber;
    }
}
