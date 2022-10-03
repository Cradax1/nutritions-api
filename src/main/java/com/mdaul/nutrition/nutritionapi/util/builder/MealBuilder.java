package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.Ingredient;
import com.mdaul.nutrition.nutritionapi.api.model.Meal;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMeal;

public class MealBuilder {

    private final FoodBuilder foodBuilder;

    public MealBuilder(FoodBuilder foodBuilder) {
        this.foodBuilder = foodBuilder;
    }

    public Meal build(CatalogueMeal catalogueMeal) {
        Meal meal = new Meal();
        meal.setName(catalogueMeal.getName());
        meal.setIngredients(catalogueMeal.getCatalogueMealIngredients().stream()
            .map(catalogueMealIngredient -> new Ingredient().gram(catalogueMealIngredient.getGram())
                .food(foodBuilder.build(catalogueMealIngredient.getCatalogueUserFood())))
                .toList());
        return meal;
    }
}
