package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.Nutriments;
import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueNutriments;

public class ApiNutrimentsBuilder {

    public Nutriments build(com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Nutriments providerNutriments) {
        Nutriments nutriments = new Nutriments();
        nutriments.setCalories(providerNutriments.getCalories());
        nutriments.setCarbohydrates(providerNutriments.getCarbohydrates());
        nutriments.setProteins(providerNutriments.getProteins());
        nutriments.setFat(providerNutriments.getFat());
        nutriments.setFiber(providerNutriments.getFiber());
        return nutriments;
    }

    public Nutriments build(CatalogueNutriments catalogueNutriments) {
        Nutriments nutriments = new Nutriments();
        nutriments.setCalories(catalogueNutriments.getCalories());
        nutriments.setCarbohydrates(catalogueNutriments.getCarbohydrates());
        nutriments.setProteins(catalogueNutriments.getProteins());
        nutriments.setFat(catalogueNutriments.getFat());
        nutriments.setFiber(catalogueNutriments.getFiber());
        return nutriments;
    }
}
