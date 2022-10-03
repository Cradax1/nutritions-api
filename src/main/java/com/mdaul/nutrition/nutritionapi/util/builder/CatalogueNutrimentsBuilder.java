package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueNutriments;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Nutriments;
import lombok.extern.slf4j.Slf4j;

import java.math.RoundingMode;

@Slf4j
public class CatalogueNutrimentsBuilder {

    public CatalogueNutriments build(Nutriments providerNutriments) {
        CatalogueNutriments catalogueNutriments = new CatalogueNutriments();
        catalogueNutriments.setCalories(providerNutriments.getCalories());
        catalogueNutriments.setCarbohydrates(providerNutriments.getCarbohydrates().setScale(2, RoundingMode.HALF_UP));
        catalogueNutriments.setProteins(providerNutriments.getProteins().setScale(2, RoundingMode.HALF_UP));
        catalogueNutriments.setFat(providerNutriments.getFat().setScale(2, RoundingMode.HALF_UP));
        catalogueNutriments.setFiber(providerNutriments.getFiber().setScale(2, RoundingMode.HALF_UP));
        return catalogueNutriments;
    }

    public CatalogueNutriments build(com.mdaul.nutrition.nutritionapi.api.model.Nutriments submissionNutriments) {
        CatalogueNutriments catalogueNutriments = new CatalogueNutriments();
        catalogueNutriments.setCalories(submissionNutriments.getCalories());
        catalogueNutriments.setCarbohydrates(submissionNutriments.getCarbohydrates().setScale(2, RoundingMode.HALF_UP));
        catalogueNutriments.setProteins(submissionNutriments.getProteins().setScale(2, RoundingMode.HALF_UP));
        catalogueNutriments.setFat(submissionNutriments.getFat().setScale(2, RoundingMode.HALF_UP));
        catalogueNutriments.setFiber(submissionNutriments.getFiber().setScale(2, RoundingMode.HALF_UP));
        return catalogueNutriments;
    }
}
