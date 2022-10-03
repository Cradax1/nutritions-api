package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.DiaryEntry;
import com.mdaul.nutrition.nutritionapi.api.model.DiaryFoodEntry;
import com.mdaul.nutrition.nutritionapi.api.model.DiaryMealEntry;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryFood;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryMeal;

import java.time.LocalDate;
import java.util.List;

public class DiaryEntryBuilder {

    private final FoodBuilder foodBuilder;
    private final MealBuilder mealBuilder;

    public DiaryEntryBuilder(FoodBuilder foodBuilder, MealBuilder mealBuilder) {
        this.foodBuilder = foodBuilder;
        this.mealBuilder = mealBuilder;
    }

    public DiaryEntry build(LocalDate assignedDay, List<DiaryFood> diaryFood, List<DiaryMeal> diaryMeal){
        DiaryEntry diaryEntry = new DiaryEntry();
        diaryEntry.setAssignedDay(assignedDay);
        diaryFood.forEach(diaryFoodItem -> diaryEntry.addFoodItem(buildDiaryFoodEntry(diaryFoodItem)));
        diaryMeal.forEach(diaryMealEntry -> diaryEntry.addMealsItem(buildDiaryMealEntry(diaryMealEntry)));
        return diaryEntry;
    }

    private DiaryFoodEntry buildDiaryFoodEntry(DiaryFood diaryFood){
        DiaryFoodEntry diaryFoodEntry = new DiaryFoodEntry();
        diaryFoodEntry.setDateTime(diaryFood.getDiaryMetaData().getDateTime());
        diaryFoodEntry.setGram(diaryFood.getGram());
        diaryFoodEntry.setFood(foodBuilder.build(diaryFood.getCatalogueUserFood()));
        return diaryFoodEntry;
    }

    private DiaryMealEntry buildDiaryMealEntry(DiaryMeal diaryMeal) {
        DiaryMealEntry diaryMealEntry = new DiaryMealEntry();
        diaryMealEntry.setDateTime(diaryMeal.getDiaryMetaData().getDateTime());
        diaryMealEntry.setPortion(diaryMeal.getPortion());
        diaryMealEntry.setMeal(mealBuilder.build(diaryMeal.getCatalogueMeal()));
        return diaryMealEntry;
    }
}
