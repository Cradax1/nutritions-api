package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueNutriments;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Nutriments;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogueNutrimentsBuilderTest {

    private final CatalogueNutrimentsBuilder catalogueNutrimentsBuilder = new CatalogueNutrimentsBuilder();
    final Nutriments testInformationProviderNutriments = Nutriments.builder()
            .calories(12)
            .carbohydrates(new BigDecimal("8.334"))
            .proteins(new BigDecimal("4.225"))
            .fat(new BigDecimal(3))
            .fiber(new BigDecimal("1.5"))
            .build();

    @Test
    void build_withInformationProviderNutriments() {
        Nutriments expectedNutriments = Nutriments.builder()
                .calories(12)
                .carbohydrates(new BigDecimal("8.33"))
                .proteins(new BigDecimal("4.23"))
                .fat(new BigDecimal("3.00"))
                .fiber(new BigDecimal("1.50"))
                .build();

        CatalogueNutriments catalogueNutriments = catalogueNutrimentsBuilder.build(testInformationProviderNutriments);

        assert_catalogueNutriments_IsEqualTo_informationProviderNutrimentsToScale2RoundHalfUp(
                catalogueNutriments, expectedNutriments);
    }

    void assert_catalogueNutriments_IsEqualTo_informationProviderNutrimentsToScale2RoundHalfUp(
            CatalogueNutriments catalogueNutriments, Nutriments informationProviderNutriments) {
        assertThat(catalogueNutriments.getCalories()).isEqualTo(informationProviderNutriments.getCalories());
        assertThat(catalogueNutriments.getCarbohydrates()).isEqualTo(
                informationProviderNutriments.getCarbohydrates().setScale(2, RoundingMode.HALF_UP));
        assertThat(catalogueNutriments.getProteins()).isEqualTo(
                informationProviderNutriments.getProteins().setScale(2, RoundingMode.HALF_UP));
        assertThat(catalogueNutriments.getFat()).isEqualTo(
                informationProviderNutriments.getFat().setScale(2, RoundingMode.HALF_UP));
        assertThat(catalogueNutriments.getFiber()).isEqualTo(
                informationProviderNutriments.getFiber().setScale(2, RoundingMode.HALF_UP));
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

        assert_catalogueNutriments_isEqualTo_apiNutrimentsToScale2RoundHalfUp(
                catalogueNutriments, submissionNutriments);
    }

    void assert_catalogueNutriments_isEqualTo_apiNutrimentsToScale2RoundHalfUp(CatalogueNutriments catalogueNutriments,
                                                                               com.mdaul.nutrition.nutritionapi.api.model.Nutriments apiNutriments) {
        assertThat(catalogueNutriments.getCalories()).isEqualTo(apiNutriments.getCalories());
        assertThat(catalogueNutriments.getCarbohydrates()).isEqualTo(
                apiNutriments.getCarbohydrates().setScale(2, RoundingMode.HALF_UP));
        assertThat(catalogueNutriments.getProteins()).isEqualTo(
                apiNutriments.getProteins().setScale(2, RoundingMode.HALF_UP));
        assertThat(catalogueNutriments.getFat()).isEqualTo(
                apiNutriments.getFat().setScale(2, RoundingMode.HALF_UP));
        assertThat(catalogueNutriments.getFiber()).isEqualTo(
                apiNutriments.getFiber().setScale(2, RoundingMode.HALF_UP));
    }
}
