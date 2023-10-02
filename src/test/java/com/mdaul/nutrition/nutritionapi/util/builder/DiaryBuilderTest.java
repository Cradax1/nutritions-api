package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.DiaryEntry;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryFood;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryMeal;
import com.mdaul.nutrition.nutritionapi.util.DataIntegrityValidator;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

class DiaryBuilderTest {

    private final ApiNutrimentsBuilder apiNutrimentsBuilder = new ApiNutrimentsBuilder();
    private final DataIntegrityValidator dataIntegrityValidator = new DataIntegrityValidator();
    private final FoodBuilder foodBuilder = new FoodBuilder(apiNutrimentsBuilder, dataIntegrityValidator);
    private final MealBuilder mealBuilder = new MealBuilder(foodBuilder);
    private final DiaryEntryBuilder diaryEntryBuilder = new DiaryEntryBuilder(foodBuilder, mealBuilder);
    private final DiaryBuilder diaryBuilder = new DiaryBuilder(diaryEntryBuilder);
    private final DiaryEntryBuilderTest diaryEntryBuilderTest = new DiaryEntryBuilderTest();

    @SuppressWarnings("squid:S2699")
    @Test
    void build_1day_2food_2meals() {
        List<DiaryFood> diaryFood = diaryEntryBuilderTest.dummyDiaryFood;
        List<DiaryMeal> diaryMeals = diaryEntryBuilderTest.dummyDiaryMeals;

        List<DiaryEntry> diary = diaryBuilder.build(diaryFood, diaryMeals);

        diaryEntryBuilderTest.asset_diaryEntry_isEqualTo_dummyAssignedDay_dummyDiaryFood_dummyDiaryMeals(diary.get(0));
    }

    @SuppressWarnings("squid:S2699")
    @Test
    void build_1day_1food() {
        List<DiaryFood> diaryFood = diaryEntryBuilderTest.dummyDiaryFood2;

        List<DiaryEntry> diary = diaryBuilder.build(diaryFood, List.of());

        diaryEntryBuilderTest.asset_diaryEntry_isEqualTo_dummyAssignedDay2_dummyDiaryFood2(diary.get(0));
    }

    @SuppressWarnings("squid:S2699")
    @Test
    void build_1day_1meal() {
        List<DiaryMeal> diaryMeals = diaryEntryBuilderTest.dummyDiaryMeals2;

        List<DiaryEntry> diary = diaryBuilder.build(List.of(), diaryMeals);

        diaryEntryBuilderTest.asset_diaryEntry_isEqualTo_dummyAssignedDay2_dummyDiaryMeals2(diary.get(0));
    }

    @SuppressWarnings("squid:S2699")
    @Test
    void build_2day_firstMeal_secondFood() {
        List<DiaryFood> diaryFood = diaryEntryBuilderTest.dummyDiaryFood;
        List<DiaryMeal> diaryMeals = diaryEntryBuilderTest.dummyDiaryMeals2;

        List<DiaryEntry> diary = diaryBuilder.build(diaryFood, diaryMeals);

        diaryEntryBuilderTest.asset_diaryEntry_isEqualTo_dummyAssignedDay2_dummyDiaryMeals2(diary.get(0));
        diaryEntryBuilderTest.asset_diaryEntry_isEqualTo_dummyAssignedDay_dummyDiaryFood(diary.get(1));
    }

    @SuppressWarnings("squid:S2699")
    @Test
    void build_2day_firstFood_secondMeal() {
        List<DiaryFood> diaryFood = diaryEntryBuilderTest.dummyDiaryFood2;
        List<DiaryMeal> diaryMeals = diaryEntryBuilderTest.dummyDiaryMeals;

        List<DiaryEntry> diary = diaryBuilder.build(diaryFood, diaryMeals);

        diaryEntryBuilderTest.asset_diaryEntry_isEqualTo_dummyAssignedDay2_dummyDiaryFood2(diary.get(0));
        diaryEntryBuilderTest.asset_diaryEntry_isEqualTo_dummyAssignedDay_dummyDiaryMeals(diary.get(1));
    }

    @SuppressWarnings("squid:S2699")
    @Test
    void build_2days_foodMealEachDay() {
        LinkedList<DiaryFood> diaryFood = new LinkedList<>(diaryEntryBuilderTest.dummyDiaryFood2);
        diaryFood.add(diaryEntryBuilderTest.dummyDiaryFood.get(0));
        diaryFood.add(diaryEntryBuilderTest.dummyDiaryFood.get(1));
        LinkedList<DiaryMeal> diaryMeals = new LinkedList<>(diaryEntryBuilderTest.dummyDiaryMeals2);
        diaryMeals.add(diaryEntryBuilderTest.dummyDiaryMeals.get(0));
        diaryMeals.add(diaryEntryBuilderTest.dummyDiaryMeals.get(1));

        List<DiaryEntry> diary = diaryBuilder.build(diaryFood, diaryMeals);

        diaryEntryBuilderTest.asset_diaryEntry_isEqualTo_dummyAssignedDay2_dummyDiaryFood2_dummyDiaryMeals2(diary.get(0));
        diaryEntryBuilderTest.asset_diaryEntry_isEqualTo_dummyAssignedDay_dummyDiaryFood_dummyDiaryMeals(diary.get(1));
    }
}