package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueNutriments;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Nutriments;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogueNutrimentsBuilderTest {

    private final CatalogueNutrimentsBuilder catalogueNutrimentsBuilder = new CatalogueNutrimentsBuilder();

    @Test
    void build_withInformationProviderNutriments() {
        Nutriments input = Nutriments.builder()
                .calories(12)
                .carbohydrates(new BigDecimal("8.334"))
                .proteins(new BigDecimal("4.225"))
                .fat(new BigDecimal(3))
                .fiber(new BigDecimal("1.5"))
                .build();

        CatalogueNutriments catalogueNutriments = catalogueNutrimentsBuilder.build(input);

        assertThat(catalogueNutriments.getCalories()).isEqualTo(12);
        assertThat(catalogueNutriments.getCarbohydrates()).isEqualTo(new BigDecimal("8.33"));
        assertThat(catalogueNutriments.getProteins()).isEqualTo(new BigDecimal("4.23"));
        assertThat(catalogueNutriments.getFat()).isEqualTo(new BigDecimal("3.00"));
        assertThat(catalogueNutriments.getFiber()).isEqualTo(new BigDecimal("1.50"));
    }

    @Test
    void build_withSubmissionNutriments() {
        com.mdaul.nutrition.nutritionapi.api.model.Nutriments submissionNutriments =
                new com.mdaul.nutrition.nutritionapi.api.model.Nutriments()
                .calories(12)
                .carbohydrates(new BigDecimal("8.334"))
                .proteins(new BigDecimal("4.225"))
                .fat(new BigDecimal(3))
                .fiber(new BigDecimal("1.5"));

        CatalogueNutriments catalogueNutriments = catalogueNutrimentsBuilder.build(submissionNutriments);

        assertThat(catalogueNutriments.getCalories()).isEqualTo(12);
        assertThat(catalogueNutriments.getCarbohydrates()).isEqualTo(new BigDecimal("8.33"));
        assertThat(catalogueNutriments.getProteins()).isEqualTo(new BigDecimal("4.23"));
        assertThat(catalogueNutriments.getFat()).isEqualTo(new BigDecimal("3.00"));
        assertThat(catalogueNutriments.getFiber()).isEqualTo(new BigDecimal("1.50"));
    }
}
