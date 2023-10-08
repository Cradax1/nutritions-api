package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.model.database.CatalogueExternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueNutriments;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.InformationProviderResult;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Nutriments;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CatalogueExternalFoodBuilderTest {

    private final CatalogueNutrimentsBuilder catalogueNutrimentsBuilder = mock(CatalogueNutrimentsBuilder.class);
    private final CatalogueExternalFoodBuilder catalogueExternalFoodBuilder = new CatalogueExternalFoodBuilder(catalogueNutrimentsBuilder);

    @Test
    void build() {
        int version = 1;
        Nutriments nutriments = Nutriments.builder()
                .calories(300)
                .carbohydrates(new BigDecimal("50.33"))
                .proteins(new BigDecimal("30.12"))
                .fat(new BigDecimal("9.34"))
                .fiber(new BigDecimal("2.84"))
                .build();
        InformationProviderResult informationProviderResult = InformationProviderResult.builder()
                .code(120983120932189L)
                .status(1)
                .product(Product.builder()
                        .productName("name")
                        .brands("brands")
                        .nutriments(nutriments)
                        .build())
                .build();
        CatalogueNutriments catalogueNutriments = CatalogueNutriments.builder().calories(300).build();
        when(catalogueNutrimentsBuilder.build(nutriments)).thenReturn(catalogueNutriments);

        CatalogueExternalFood catalogueExternalFood = catalogueExternalFoodBuilder.build(informationProviderResult, version);

        assertThat(catalogueExternalFood.getBarcode()).isEqualTo(informationProviderResult.getCode());
        assertThat(catalogueExternalFood.getVersion()).isEqualTo(version);
        assertThat(catalogueExternalFood.getCatalogueFood().getBrands()).isEqualTo(informationProviderResult.getProduct().getBrands());
        assertThat(catalogueExternalFood.getCatalogueFood().getNutriments()).isEqualTo(catalogueNutriments);
    }
}
