package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.model.database.CatalogueExternalFood;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.InformationProviderResult;

public class CatalogueExternalFoodBuilder {

    private final CatalogueNutrimentsBuilder catalogueNutrimentsBuilder;

    public CatalogueExternalFoodBuilder(CatalogueNutrimentsBuilder catalogueNutrimentsBuilder) {
        this.catalogueNutrimentsBuilder = catalogueNutrimentsBuilder;
    }

    public CatalogueExternalFood build(InformationProviderResult informationProviderResult, int version) {
        CatalogueExternalFood catalogueExternalFood = new CatalogueExternalFood();
        catalogueExternalFood.setBarcode(informationProviderResult.getCode());
        catalogueExternalFood.setVersion(version);
        catalogueExternalFood.getCatalogueFood().setBrands(informationProviderResult.getProduct().getBrands());
        catalogueExternalFood.getCatalogueFood()
            .setNutriments(catalogueNutrimentsBuilder.build(informationProviderResult.getProduct().getNutriments()));
        return catalogueExternalFood;
    }
}
