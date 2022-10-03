package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.model.database.CatalogueExternalFood;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.InformationProviderResult;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Nutriments;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogueExternalFoodBuilderTest {

    private final CatalogueNutrimentsBuilder catalogueNutrimentsBuilder = new CatalogueNutrimentsBuilder();
    private final CatalogueExternalFoodBuilder catalogueExternalFoodBuilder = new CatalogueExternalFoodBuilder(catalogueNutrimentsBuilder);
    private final CatalogueNutrimentsBuilderTest catalogueNutrimentsBuilderTest = new CatalogueNutrimentsBuilderTest();

    @Test
    void build() {
        Nutriments providerNutriments = catalogueNutrimentsBuilderTest.testInformationProviderNutriments;
        InformationProviderResult informationProviderResult = InformationProviderResult.builder()
                .code(120983120932189L)
                .status(1)
                .product(Product.builder()
                        .productName("Chilli")
                        .brands("brand1, brand2")
                        .nutriments(providerNutriments)
                        .build())
                .build();

        CatalogueExternalFood catalogueExternalFood = catalogueExternalFoodBuilder.build(informationProviderResult, 1);

        assertThat(catalogueExternalFood.getCatalogueFood().getBrands()).isEqualTo(informationProviderResult.getProduct().getBrands());
        catalogueNutrimentsBuilderTest.assert_catalogueNutriments_IsEqualTo_informationProviderNutrimentsToScale2RoundHalfUp(
                catalogueExternalFood.getCatalogueFood().getNutriments(), providerNutriments);
    }
}
