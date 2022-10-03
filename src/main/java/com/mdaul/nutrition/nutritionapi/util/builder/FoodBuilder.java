package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.Food;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueExternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueInternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.InformationProviderResult;
import com.mdaul.nutrition.nutritionapi.util.DataIntegrityValidator;

public class FoodBuilder {

    private final ApiNutrimentsBuilder apiNutrimentsBuilder;
    private final DataIntegrityValidator dataIntegrityValidator;

    public FoodBuilder(ApiNutrimentsBuilder apiNutrimentsBuilder,
                       DataIntegrityValidator dataIntegrityValidator) {
        this.apiNutrimentsBuilder = apiNutrimentsBuilder;
        this.dataIntegrityValidator = dataIntegrityValidator;
    }

    public Food build(InformationProviderResult informationProviderResult) {
        Food food = new Food();
        food.setName(informationProviderResult.getProduct()
            .getProductName());
        food.setBrands(informationProviderResult.getProduct()
            .getBrands());
        food.setNutriments(apiNutrimentsBuilder.build(informationProviderResult.getProduct().getNutriments()));
        return food;
    }

    public Food build(CatalogueUserFood catalogueUserFood) {
        dataIntegrityValidator.validateContainsExactlyOneFoodType(catalogueUserFood);
        Food food = new Food();
        if (catalogueUserFood.getCatalogueExternalFood()!=null) {
            addExternalFood(food, catalogueUserFood.getCatalogueExternalFood());
        } else if (catalogueUserFood.getCatalogueInternalFood()!=null) {
            addInternalFood(food, catalogueUserFood.getCatalogueInternalFood());
        }
        food.setName(catalogueUserFood.getName());
        return food;
    }

    public Food build(CatalogueInternalFood catalogueInternalFood) {
        Food food = new Food();
        food.setName(catalogueInternalFood.getCatalogueUserFood().getName());
        addInternalFood(food, catalogueInternalFood);
        return food;
    }

    private void addExternalFood(Food food, CatalogueExternalFood catalogueExternalFood) {
        food.setBrands(catalogueExternalFood.getCatalogueFood()
            .getBrands());
        food.setNutriments(apiNutrimentsBuilder.build(catalogueExternalFood.getCatalogueFood()
            .getNutriments()));
    }

    private void addInternalFood(Food food, CatalogueInternalFood catalogueInternalFood) {
        food.setBrands(catalogueInternalFood.getCatalogueFood()
            .getBrands());
        food.setNutriments(apiNutrimentsBuilder.build(catalogueInternalFood.getCatalogueFood()
            .getNutriments()));
    }
}
