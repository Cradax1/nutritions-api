package com.mdaul.nutrition.nutritionapi.util;

import com.mdaul.nutrition.nutritionapi.api.model.Ingredient;
import com.mdaul.nutrition.nutritionapi.api.model.Meal;

public class DishTemplateUtils {

    public int getMealCalories(Meal meal) {
        int calories = 0;
        for (Ingredient ingredient : meal.getIngredients()) {
            calories += (int) Math.round(
                    Double.valueOf(ingredient.getFood().getNutriments().getCalories()) / 100
                    * Double.valueOf(ingredient.getGram()));
        }
        return calories;
    }
}
