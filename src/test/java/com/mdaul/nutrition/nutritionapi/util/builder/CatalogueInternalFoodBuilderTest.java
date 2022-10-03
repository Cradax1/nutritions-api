package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.Nutriments;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueInternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogueInternalFoodBuilderTest {

    private final CatalogueNutrimentsBuilder catalogueNutrimentsBuilder = new CatalogueNutrimentsBuilder();
    private final CatalogueInternalFoodBuilder catalogueInternalFoodBuilder =
            new CatalogueInternalFoodBuilder(catalogueNutrimentsBuilder);
    private final CatalogueNutrimentsBuilderTest catalogueNutrimentsBuilderTest =
            new CatalogueNutrimentsBuilderTest();

    @Test
    void build() {
        String brands = "brand1, brand2";
        Nutriments nutriments = new Nutriments()
                        .calories(52)
                        .carbohydrates(new BigDecimal("32.3"))
                        .proteins(new BigDecimal("43.27"))
                        .fat(new BigDecimal("5.3"))
                        .fiber(new BigDecimal(1));
        CatalogueUserFood catalogueUserFood = CatalogueUserFood.builder()
                .id(822323333333L)
                .userId("8das89fdf")
                .active(true)
                .build();

        CatalogueInternalFood catalogueInternalFood =
                catalogueInternalFoodBuilder.build(brands, nutriments, catalogueUserFood);

        assertThat(catalogueInternalFood.getCatalogueUserFood().getId()).isEqualTo(catalogueUserFood.getId());
        assertThat(catalogueInternalFood.getCatalogueUserFood().getUserId()).isEqualTo(catalogueUserFood.getUserId());
        assertThat(catalogueInternalFood.getCatalogueUserFood().isActive()).isEqualTo(catalogueUserFood.isActive());
        assertThat(catalogueInternalFood.getCatalogueFood().getBrands()).isEqualTo(brands);
        catalogueNutrimentsBuilderTest.assert_catalogueNutriments_isEqualTo_apiNutrimentsToScale2RoundHalfUp(
                catalogueInternalFood.getCatalogueFood().getNutriments(), nutriments);
    }
}
