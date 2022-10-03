package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueNutriments;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Nutriments;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ApiNutrimentsBuilderTest {

    private final ApiNutrimentsBuilder apiNutrimentsBuilder = new ApiNutrimentsBuilder();

    @Test
    void buildTest_withInformationProviderResult() {
        Nutriments informationProviderNutriments = Nutriments.builder()
                .calories(12)
                .carbohydrates(new BigDecimal("8.33"))
                .proteins(new BigDecimal("4.2"))
                .fat(new BigDecimal(3))
                .fiber(new BigDecimal("1.5"))
                .build();

        com.mdaul.nutrition.nutritionapi.api.model.Nutriments nutriments = apiNutrimentsBuilder.build(informationProviderNutriments);

        assertThat(nutriments.getCalories()).isEqualTo(informationProviderNutriments.getCalories());
        assertThat(nutriments.getCarbohydrates()).isEqualTo(informationProviderNutriments.getCarbohydrates());
        assertThat(nutriments.getProteins()).isEqualTo(informationProviderNutriments.getProteins());
        assertThat(nutriments.getFat()).isEqualTo(informationProviderNutriments.getFat());
        assertThat(nutriments.getFiber()).isEqualTo(informationProviderNutriments.getFiber());
    }

    @Test
    void buildTest_withCatalogueNutriments() {
        CatalogueNutriments catalogueNutriments = CatalogueNutriments.builder()
                .calories(12)
                .carbohydrates(new BigDecimal("8.33"))
                .proteins(new BigDecimal("4.2"))
                .fat(new BigDecimal(3))
                .fiber(new BigDecimal("1.5"))
                .build();

        com.mdaul.nutrition.nutritionapi.api.model.Nutriments nutriments = apiNutrimentsBuilder.build(catalogueNutriments);

        assertThat(nutriments.getCalories()).isEqualTo(catalogueNutriments.getCalories());
        assertThat(nutriments.getCarbohydrates()).isEqualTo(catalogueNutriments.getCarbohydrates());
        assertThat(nutriments.getProteins()).isEqualTo(catalogueNutriments.getProteins());
        assertThat(nutriments.getFat()).isEqualTo(catalogueNutriments.getFat());
        assertThat(nutriments.getFiber()).isEqualTo(catalogueNutriments.getFiber());
    }
}
