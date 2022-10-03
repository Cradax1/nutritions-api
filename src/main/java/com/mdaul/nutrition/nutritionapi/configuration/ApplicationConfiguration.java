package com.mdaul.nutrition.nutritionapi.configuration;

import com.mdaul.nutrition.nutritionapi.client.FoodInformationProviderClient;
import com.mdaul.nutrition.nutritionapi.util.DataEntriesOptimizer;
import com.mdaul.nutrition.nutritionapi.util.DataIntegrityValidator;
import com.mdaul.nutrition.nutritionapi.util.builder.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public FoodInformationProviderClient foodInformationProviderClient(FoodInformationProviderConfiguration providerConfiguration) {
        return new FoodInformationProviderClient(providerConfiguration);
    }

    @Bean
    public DataIntegrityValidator catalogueUserFoodValidator() {
        return new DataIntegrityValidator();
    }

    @Bean
    public DataEntriesOptimizer dataEntriesOptimizer(DataIntegrityValidator dataIntegrityValidator) {
        return new DataEntriesOptimizer(dataIntegrityValidator);
    }

    @Bean
    public FoodBuilder foodBuilder(ApiNutrimentsBuilder apiNutrimentsBuilder, DataIntegrityValidator dataIntegrityValidator) {
        return new FoodBuilder(apiNutrimentsBuilder, dataIntegrityValidator);
    }

    @Bean
    public ApiNutrimentsBuilder apiNutrimentsBuilder() {
        return new ApiNutrimentsBuilder();
    }

    @Bean
    public CatalogueExternalFoodBuilder catalogueExternalFoodBuilder(CatalogueNutrimentsBuilder catalogueNutrimentsBuilder) {
        return new CatalogueExternalFoodBuilder(catalogueNutrimentsBuilder);
    }

    @Bean
    public CatalogueInternalFoodBuilder catalogueInternalFoodBuilder(CatalogueNutrimentsBuilder catalogueNutrimentsBuilder) {
        return new CatalogueInternalFoodBuilder(catalogueNutrimentsBuilder);
    }

    @Bean
    public CatalogueNutrimentsBuilder catalogueNutrimentsBuilder() {
        return new CatalogueNutrimentsBuilder();
    }

    @Bean
    public MealBuilder mealBuilder(FoodBuilder foodBuilder) {
        return new MealBuilder(foodBuilder);
    }

    @Bean
    public DiaryEntryBuilder diaryEntryBuilder(FoodBuilder foodBuilder, MealBuilder mealBuilder) {
        return new DiaryEntryBuilder(foodBuilder, mealBuilder);
    }
}
