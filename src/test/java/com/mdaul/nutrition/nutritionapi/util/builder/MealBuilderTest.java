package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.Food;
import com.mdaul.nutrition.nutritionapi.api.model.Meal;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMeal;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMealIngredient;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MealBuilderTest {

    private final FoodBuilder foodBuilder = mock(FoodBuilder.class);
    private final MealBuilder mealBuilder = new MealBuilder(foodBuilder);

    @Test
    void build() {
        Food food1 = new Food().name("food1");
        Food food2 = new Food().name("food2");
        CatalogueUserFood catalogueUserFood1 = CatalogueUserFood.builder().name("userFood1").build();
        CatalogueUserFood catalogueUserFood2 = CatalogueUserFood.builder().name("userFood2").build();
        CatalogueMeal dummyCatalogueMeal = CatalogueMeal.builder()
                .id(8291938211231L)
                .name("mealName")
                .userId("809af8s90a8fd09a8sd")
                .active(true)
                .catalogueMealIngredients(List.of(
                        CatalogueMealIngredient.builder()
                                .gram(23)
                                .catalogueUserFood(catalogueUserFood1)
                                .build()
                        , CatalogueMealIngredient.builder()
                                .gram(47)
                                .catalogueUserFood(catalogueUserFood2)
                                .build()))
                .build();
        when(foodBuilder.build(catalogueUserFood1)).thenReturn(food1);
        when(foodBuilder.build(catalogueUserFood2)).thenReturn(food2);

        Meal meal = mealBuilder.build(dummyCatalogueMeal);

        assertThat(meal.getName()).isEqualTo(dummyCatalogueMeal.getName());
        assertThat(meal.getIngredients().get(0).getGram())
                .isEqualTo(dummyCatalogueMeal.getCatalogueMealIngredients().get(0).getGram());
        assertThat(meal.getIngredients().get(0).getFood()).isEqualTo(food1);
        assertThat(meal.getIngredients().get(1).getGram())
                .isEqualTo(dummyCatalogueMeal.getCatalogueMealIngredients().get(1).getGram());
        assertThat(meal.getIngredients().get(1).getFood()).isEqualTo(food2);
    }
}
