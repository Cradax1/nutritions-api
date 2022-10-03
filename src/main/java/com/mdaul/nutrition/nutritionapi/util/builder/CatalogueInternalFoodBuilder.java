package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.Nutriments;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueInternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;

public class CatalogueInternalFoodBuilder {

    private final CatalogueNutrimentsBuilder catalogueNutrimentsBuilder;

    public CatalogueInternalFoodBuilder(CatalogueNutrimentsBuilder catalogueNutrimentsBuilder) {
        this.catalogueNutrimentsBuilder = catalogueNutrimentsBuilder;
    }

    public CatalogueInternalFood build(String brands, Nutriments nutriments, CatalogueUserFood catalogueUserFood) {
        CatalogueInternalFood catalogueInternalFood = new CatalogueInternalFood();
        catalogueInternalFood.getCatalogueFood()
            .setBrands(brands);
        catalogueInternalFood.getCatalogueFood()
            .setNutriments(catalogueNutrimentsBuilder.build(nutriments));
        catalogueInternalFood.setCatalogueUserFood(catalogueUserFood);
        return catalogueInternalFood;
    }
}
