package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.Food;
import com.mdaul.nutrition.nutritionapi.exception.DataIntegrityException;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueExternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueInternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueFood;
import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueNutriments;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.InformationProviderResult;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Nutriments;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Product;
import com.mdaul.nutrition.nutritionapi.util.DataIntegrityValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FoodBuilderTest {

    private static final String DUMMY_USER_ID = "809af8s90a8fd09a8sd";
    private final ApiNutrimentsBuilder apiNutrimentsBuilder = new ApiNutrimentsBuilder();
    private final DataIntegrityValidator dataIntegrityValidator = new DataIntegrityValidator();
    private final FoodBuilder foodBuilder = new FoodBuilder(apiNutrimentsBuilder, dataIntegrityValidator);

    private final CatalogueFood dummyCatalogueFood = CatalogueFood.builder()
            .brands("brand1, brand2")
            .nutriments(CatalogueNutriments.builder()
                    .calories(213)
                    .carbohydrates(new BigDecimal(50))
                    .proteins(new BigDecimal("32.4"))
                    .fat(new BigDecimal("12.06"))
                    .fiber(new BigDecimal(5))
                    .build())
            .build();

    private final CatalogueFood dummyCatalogueFood2 = CatalogueFood.builder()
            .brands("brand3, brand4")
            .nutriments(CatalogueNutriments.builder()
                    .calories(492)
                    .carbohydrates(new BigDecimal(94))
                    .proteins(new BigDecimal("20.4"))
                    .fat(new BigDecimal("9.06"))
                    .fiber(new BigDecimal(10))
                    .build())
            .build();

    private final CatalogueUserFood dummyCatalogueUserFoodWithoutFood = CatalogueUserFood.builder()
            .id(2013093183131L)
            .userId(DUMMY_USER_ID)
            .name("foodName")
            .active(true)
            .build();

    private final CatalogueUserFood dummyCatalogueUserFoodInternal = CatalogueUserFood.builder()
            .id(2013902132131L)
            .userId(DUMMY_USER_ID)
            .name("foodName1")
            .active(true)
            .catalogueInternalFood(CatalogueInternalFood.builder()
                    .id(2131321321123L)
                    .catalogueFood(dummyCatalogueFood)
                    .build())
            .build();

    private final CatalogueUserFood dummyCatalogueUserFoodExternal = CatalogueUserFood.builder()
            .id(20232313323231L)
            .userId(DUMMY_USER_ID)
            .name("foodName2")
            .active(false)
            .catalogueExternalFood(CatalogueExternalFood.builder()
                    .barcode(2131321321123L)
                    .catalogueFood(dummyCatalogueFood2)
                    .build())
            .build();

    @Test
    void build_withInformationProviderResult() {
        InformationProviderResult providerResult = InformationProviderResult.builder()
                .code(2131321321123L)
                .status(1)
                .product(Product.builder()
                        .brands("brand1, brand2")
                        .productName("name")
                        .nutriments(Nutriments.builder()
                                .calories(232)
                                .carbohydrates(new BigDecimal(32))
                                .proteins(new BigDecimal("8.31"))
                                .fat(new BigDecimal("5.7"))
                                .fiber(new BigDecimal(2))
                                .build())
                        .build())
                .build();

        Food food = foodBuilder.build(providerResult);

        assertThat(food.getName()).isEqualTo(providerResult.getProduct().getProductName());
        assertThat(food.getBrands()).isEqualTo(providerResult.getProduct().getBrands());
        assertFoodNutrimentsIsEqualToProviderResultNutriments(food.getNutriments(), providerResult.getProduct().getNutriments());
    }

    private void assertFoodNutrimentsIsEqualToProviderResultNutriments(com.mdaul.nutrition.nutritionapi.api.model.Nutriments foodNutriments, Nutriments providerNutriments) {
        assertThat(foodNutriments.getCalories()).isEqualTo(providerNutriments.getCalories());
        assertThat(foodNutriments.getCarbohydrates()).isEqualTo(providerNutriments.getCarbohydrates());
        assertThat(foodNutriments.getProteins()).isEqualTo(providerNutriments.getProteins());
        assertThat(foodNutriments.getFat()).isEqualTo(providerNutriments.getFat());
        assertThat(foodNutriments.getFiber()).isEqualTo(providerNutriments.getFiber());
    }

    @Test
    void build_withCatalogueUserFoodContainingOnlyInternalFood() {
        CatalogueUserFood catalogueUserFood = dummyCatalogueUserFoodInternal;

        Food food = foodBuilder.build(catalogueUserFood);

        assert_Food_isEqualTo_catalogueUserFoodInternal(food, catalogueUserFood);
    }

    private void assert_Food_isEqualTo_catalogueUserFoodInternal(Food food, CatalogueUserFood catalogueUserFood) {
        assertThat(food.getName()).isEqualTo(catalogueUserFood.getName());
        assertThat(food.getBrands()).isEqualTo(
                catalogueUserFood.getCatalogueInternalFood().getCatalogueFood().getBrands());
        assertFoodNutrimentsIsEqualToCatalogueNutriments(
                food.getNutriments(), catalogueUserFood.getCatalogueInternalFood().getCatalogueFood().getNutriments());
    }

    @Test
    void build_withCatalogueUserFood_containingOnlyExternalFood() {
        CatalogueUserFood catalogueUserFood = dummyCatalogueUserFoodExternal;

        Food food = foodBuilder.build(catalogueUserFood);

        assertFoodIsEqualToCatalogueUserFoodExternal(food, catalogueUserFood);
    }

    private void assertFoodIsEqualToCatalogueUserFoodExternal(Food food, CatalogueUserFood catalogueUserFoodExternal) {
        assertThat(food.getName()).isEqualTo(catalogueUserFoodExternal.getName());
        assertThat(food.getBrands()).isEqualTo(
                catalogueUserFoodExternal.getCatalogueExternalFood().getCatalogueFood().getBrands());
        assertFoodNutrimentsIsEqualToCatalogueNutriments(
                food.getNutriments(), catalogueUserFoodExternal.getCatalogueExternalFood().getCatalogueFood().getNutriments());
    }

    @Test
    void build_withCatalogueUserFood_containingInternalAndExternalFood() {
        CatalogueUserFood catalogueUserFood = CatalogueUserFood.builder()
                .catalogueExternalFood(new CatalogueExternalFood())
                .catalogueInternalFood(new CatalogueInternalFood())
                .build();

        assertThatThrownBy(() ->
                foodBuilder.build(catalogueUserFood))
                .isInstanceOf(DataIntegrityException.class);
    }

    @Test
    void build_withCatalogueUserFood_containingNoFood() {
        CatalogueUserFood catalogueUserFood = CatalogueUserFood.builder()
                .id(12903210893209131L)
                .userId("09dsa8f90sd09f09afs")
                .active(true)
                .name("foodName")
                .build();

        assertThatThrownBy(() ->
                foodBuilder.build(catalogueUserFood))
                .isInstanceOf(DataIntegrityException.class);
    }

    @Test
    void build_withCatalogueInternalFood() {
        CatalogueInternalFood catalogueInternalFood = CatalogueInternalFood.builder()
                .catalogueUserFood(dummyCatalogueUserFoodWithoutFood)
                .catalogueFood(dummyCatalogueFood)
                .build();

        Food food = foodBuilder.build(catalogueInternalFood);

        assertThat(food.getName()).isEqualTo(catalogueInternalFood.getCatalogueUserFood().getName());
        assertThat(food.getBrands()).isEqualTo(catalogueInternalFood.getCatalogueFood().getBrands());
        assertFoodNutrimentsIsEqualToCatalogueNutriments(
                food.getNutriments(), catalogueInternalFood.getCatalogueFood().getNutriments());
    }

    private void assertFoodNutrimentsIsEqualToCatalogueNutriments(com.mdaul.nutrition.nutritionapi.api.model.Nutriments foodNutriments,
                                                                  CatalogueNutriments catalogueNutriments) {
        assertThat(foodNutriments.getCalories()).isEqualTo(catalogueNutriments.getCalories());
        assertThat(foodNutriments.getCarbohydrates()).isEqualTo(catalogueNutriments.getCarbohydrates());
        assertThat(foodNutriments.getProteins()).isEqualTo(catalogueNutriments.getProteins());
        assertThat(foodNutriments.getFat()).isEqualTo(catalogueNutriments.getFat());
        assertThat(foodNutriments.getFiber()).isEqualTo(catalogueNutriments.getFiber());
    }
}
