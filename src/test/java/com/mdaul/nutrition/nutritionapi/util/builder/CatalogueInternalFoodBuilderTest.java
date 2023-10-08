package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.Nutriments;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueInternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueNutriments;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CatalogueInternalFoodBuilderTest {

    private final CatalogueNutrimentsBuilder catalogueNutrimentsBuilder = mock(CatalogueNutrimentsBuilder.class);
    private final CatalogueInternalFoodBuilder catalogueInternalFoodBuilder =
            new CatalogueInternalFoodBuilder(catalogueNutrimentsBuilder);

    @Test
    void build() {
        String brands = "brand1, brand2";
        Nutriments nutriments = new Nutriments()
                        .calories(52)
                        .carbohydrates(new BigDecimal("32.83"))
                        .proteins(new BigDecimal("43.27"))
                        .fat(new BigDecimal("5.31"))
                        .fiber(new BigDecimal("1.48"));
        CatalogueUserFood catalogueUserFood = CatalogueUserFood.builder()
                .id(822323333333L)
                .userId("8das89fdf")
                .active(true)
                .build();
        CatalogueNutriments catalogueNutriments = CatalogueNutriments.builder().calories(300).build();
        when(catalogueNutrimentsBuilder.build(nutriments)).thenReturn(catalogueNutriments);

        CatalogueInternalFood catalogueInternalFood =
                catalogueInternalFoodBuilder.build(brands, nutriments, catalogueUserFood);

        assertThat(catalogueInternalFood.getCatalogueUserFood().getId()).isEqualTo(catalogueUserFood.getId());
        assertThat(catalogueInternalFood.getCatalogueUserFood().getUserId()).isEqualTo(catalogueUserFood.getUserId());
        assertThat(catalogueInternalFood.getCatalogueUserFood().isActive()).isEqualTo(catalogueUserFood.isActive());
        assertThat(catalogueInternalFood.getCatalogueFood().getBrands()).isEqualTo(brands);
        assertThat(catalogueInternalFood.getCatalogueFood().getNutriments()).isEqualTo(catalogueNutriments);
    }
}
