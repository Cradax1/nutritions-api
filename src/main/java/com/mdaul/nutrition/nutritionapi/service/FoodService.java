package com.mdaul.nutrition.nutritionapi.service;

import com.mdaul.nutrition.nutritionapi.api.model.Food;
import com.mdaul.nutrition.nutritionapi.client.FoodInformationProviderClient;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.InformationProviderResult;
import com.mdaul.nutrition.nutritionapi.util.builder.FoodBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class FoodService {

    private final FoodInformationProviderClient providerClient;
    private final FoodBuilder foodBuilder;

    public FoodService(FoodInformationProviderClient providerClient, FoodBuilder foodBuilder) {
        this.providerClient = providerClient;
        this.foodBuilder = foodBuilder;
    }

    public Optional<Food> getFood(long barcode) {
        log.info("Searching for food with barcode {} at information provider", barcode);
        Optional<InformationProviderResult> result = providerClient.getFoodInformation(barcode);
        if (result.isEmpty()) {
            log.info("Could not find food with barcode {} at information provider", barcode);
            return Optional.empty();
        }
        return Optional.of(foodBuilder.build(result.get()));
    }
}
