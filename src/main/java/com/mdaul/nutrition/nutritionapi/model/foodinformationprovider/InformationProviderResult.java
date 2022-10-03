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
public class InformationProviderResult {
    public static final int STATUS_ITEM_NOT_FOUND = 0;
    public static final int STATUS_ITEM_FOUND = 1;

    private Long code;
    private Product product;
    private Integer status;
    @JsonProperty(value = "status_verbose")
    private String statusVerbose;

    @JsonCreator
    public InformationProviderResult(
            @JsonProperty(required = true) Long code, @JsonProperty Product product,
            @JsonProperty(required = true) Integer status, @JsonProperty(value = "status_verbose") String statusVerbose) {
        if (code == null || status == null) {
            throw new FoodInformationProviderItemException(
                    "Cannot build InformationProviderResult because a required parameter is null");
        }
        this.code = code;
        this.product = product;
        this.status = status;
        this.statusVerbose = statusVerbose;
    }
}
