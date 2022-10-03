package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.Meal;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMeal;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMealIngredient;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import com.mdaul.nutrition.nutritionapi.util.DataIntegrityValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MealBuilderTest {

    private final ApiNutrimentsBuilder apiNutrimentsBuilder = new ApiNutrimentsBuilder();
    private final DataIntegrityValidator dataIntegrityValidator = new DataIntegrityValidator();
    private final FoodBuilder foodBuilder = new FoodBuilder(apiNutrimentsBuilder, dataIntegrityValidator);
    private final MealBuilder mealBuilder = new MealBuilder(foodBuilder);
    private final FoodBuilderTest foodBuilderTest = new FoodBuilderTest();

    final CatalogueUserFood dummyCatalogueUserFoodInternal = foodBuilderTest.dummyCatalogueUserFoodInternal;
    final CatalogueUserFood dummyCatalogueUserFoodExternal = foodBuilderTest.dummyCatalogueUserFoodExternal;

    final CatalogueMeal dummyCatalogueMeal = CatalogueMeal.builder()
            .id(8291938211231L)
            .name("mealName")
            .userId(dummyCatalogueUserFoodInternal.getUserId())
            .active(true)
            .catalogueMealIngredients(List.of(
                    CatalogueMealIngredient.builder()
                            .gram(23)
                            .catalogueUserFood(dummyCatalogueUserFoodExternal)
                            .build()
                    , CatalogueMealIngredient.builder()
                            .gram(47)
                            .catalogueUserFood(dummyCatalogueUserFoodInternal)
                            .build()))
            .build();

    final CatalogueMeal dummyCatalogueMeal2 = CatalogueMeal.builder()
            .id(829193123894131L)
            .name("mealName2")
            .userId(dummyCatalogueUserFoodInternal.getUserId())
            .active(false)
            .catalogueMealIngredients(List.of(
                    CatalogueMealIngredient.builder()
                            .gram(64)
                            .catalogueUserFood(dummyCatalogueUserFoodExternal)
                            .build()
                    , CatalogueMealIngredient.builder()
                            .gram(85)
                            .catalogueUserFood(dummyCatalogueUserFoodInternal)
                            .build()))
            .build();

    @Test
    void build() {
        Meal meal = mealBuilder.build(dummyCatalogueMeal);

        assert_meal_isEqualTo_dummyCatalogueMeal(meal);
    }

    void assert_meal_isEqualTo_dummyCatalogueMeal(Meal meal) {
        //meal name
        assertThat(meal.getName()).isEqualTo(dummyCatalogueMeal.getName());

        //  ---  CatalogueUserFoodExternal  ---   //
        assertThat(meal.getIngredients().get(0).getGram())
                .isEqualTo(dummyCatalogueMeal.getCatalogueMealIngredients().get(0).getGram());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodExternal(
                meal.getIngredients().get(0).getFood(),
                dummyCatalogueMeal.getCatalogueMealIngredients().get(0).getCatalogueUserFood());

        //  ---  CatalogueUserFoodInternal  ---   //
        assertThat(meal.getIngredients().get(1).getGram())
                .isEqualTo(dummyCatalogueMeal.getCatalogueMealIngredients().get(1).getGram());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodInternal(
                meal.getIngredients().get(1).getFood(),
                dummyCatalogueMeal.getCatalogueMealIngredients().get(1).getCatalogueUserFood());
    }

    void assert_meal_isEqualTo_dummyCatalogueMeal2(Meal meal) {
        //meal name
        assertThat(meal.getName()).isEqualTo(dummyCatalogueMeal2.getName());

        //  ---  CatalogueUserFoodExternal  ---   //
        assertThat(meal.getIngredients().get(0).getGram())
                .isEqualTo(dummyCatalogueMeal2.getCatalogueMealIngredients().get(0).getGram());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodExternal(
                meal.getIngredients().get(0).getFood(),
                dummyCatalogueMeal2.getCatalogueMealIngredients().get(0).getCatalogueUserFood());

        //  ---  CatalogueUserFoodInternal  ---   //
        assertThat(meal.getIngredients().get(1).getGram())
                .isEqualTo(dummyCatalogueMeal2.getCatalogueMealIngredients().get(1).getGram());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodInternal(
                meal.getIngredients().get(1).getFood(),
                dummyCatalogueMeal2.getCatalogueMealIngredients().get(1).getCatalogueUserFood());
    }
}
