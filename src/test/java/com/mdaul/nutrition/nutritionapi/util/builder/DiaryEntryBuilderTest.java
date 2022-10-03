package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.DiaryEntry;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryFood;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryMeal;
import com.mdaul.nutrition.nutritionapi.model.database.embedded.DiaryMetaData;
import com.mdaul.nutrition.nutritionapi.util.DataIntegrityValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DiaryEntryBuilderTest {

    private final ApiNutrimentsBuilder apiNutrimentsBuilder = new ApiNutrimentsBuilder();
    private final DataIntegrityValidator dataIntegrityValidator = new DataIntegrityValidator();
    private final FoodBuilder foodBuilder = new FoodBuilder(apiNutrimentsBuilder, dataIntegrityValidator);
    private final MealBuilder mealBuilder = new MealBuilder(foodBuilder);
    private final DiaryEntryBuilder diaryEntryBuilder = new DiaryEntryBuilder(foodBuilder, mealBuilder);
    private final FoodBuilderTest foodBuilderTest = new FoodBuilderTest();
    private final MealBuilderTest mealBuilderTest = new MealBuilderTest();
    private final CatalogueUserFood dummyCatalogueUserFoodInternal = foodBuilderTest.dummyCatalogueUserFoodInternal;
    private final CatalogueUserFood dummyCatalogueUserFoodExternal = foodBuilderTest.dummyCatalogueUserFoodExternal;

    @Test
    void build() {
        String userId = dummyCatalogueUserFoodInternal.getUserId();
        LocalDate assignedDay = LocalDate.of(2022, 9, 23);

        List<DiaryFood> diaryFood = List.of(DiaryFood.builder()
                        .id(982348928948293L)
                        .diaryMetaData(DiaryMetaData.builder()
                                .userId(userId)
                                .assignedDay(assignedDay)
                                .dateTime(LocalDateTime.of(assignedDay, LocalTime.of(9, 32, 2)))
                                .build())
                        .catalogueUserFood(dummyCatalogueUserFoodInternal)
                        .gram(94)
                .build(),
                DiaryFood.builder()
                        .id(1232132138293L)
                        .diaryMetaData(DiaryMetaData.builder()
                                .userId(userId)
                                .assignedDay(assignedDay)
                                .dateTime(LocalDateTime.of(assignedDay.plusDays(1), LocalTime.of(2, 1, 43)))
                                .build())
                        .catalogueUserFood(dummyCatalogueUserFoodExternal)
                        .gram(87)
                        .build());

        List<DiaryMeal> diaryMeals = List.of(DiaryMeal.builder()
                        .id(121209099033L)
                        .diaryMetaData(DiaryMetaData.builder()
                                .userId(userId)
                                .assignedDay(assignedDay)
                                .dateTime(LocalDateTime.of(assignedDay, LocalTime.of(12, 15, 41)))
                                .build())
                        .catalogueMeal(mealBuilderTest.dummyCatalogueMeal)
                        .portion(new BigDecimal("1.6"))
                .build(),
                DiaryMeal.builder()
                        .id(121209099033L)
                        .diaryMetaData(DiaryMetaData.builder()
                                .userId(userId)
                                .assignedDay(assignedDay)
                                .dateTime(LocalDateTime.of(assignedDay.plusDays(1), LocalTime.of(0, 23, 51)))
                                .build())
                        .catalogueMeal(mealBuilderTest.dummyCatalogueMeal2)
                        .portion(new BigDecimal("0.8"))
                        .build());

        DiaryEntry diaryEntry = diaryEntryBuilder.build(assignedDay, diaryFood, diaryMeals);

        assertThat(diaryEntry.getAssignedDay()).isEqualTo(assignedDay);

        assertThat(diaryEntry.getFood().get(0).getGram()).isEqualTo(diaryFood.get(0).getGram());
        assertThat(diaryEntry.getFood().get(0).getDateTime()).isEqualTo(diaryFood.get(0).getDiaryMetaData().getDateTime());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodInternal(
                diaryEntry.getFood().get(0).getFood(), diaryFood.get(0).getCatalogueUserFood());

        assertThat(diaryEntry.getFood().get(1).getGram()).isEqualTo(diaryFood.get(1).getGram());
        assertThat(diaryEntry.getFood().get(1).getDateTime()).isEqualTo(diaryFood.get(1).getDiaryMetaData().getDateTime());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodExternal(
                diaryEntry.getFood().get(1).getFood(), diaryFood.get(1).getCatalogueUserFood());

        assertThat(diaryEntry.getMeals().get(0).getDateTime()).isEqualTo(diaryMeals.get(0).getDiaryMetaData().getDateTime());
        assertThat(diaryEntry.getMeals().get(0).getPortion()).isEqualTo(diaryMeals.get(0).getPortion());
        mealBuilderTest.assert_meal_isEqualTo_dummyCatalogueMeal(diaryEntry.getMeals().get(0).getMeal());

        assertThat(diaryEntry.getMeals().get(1).getDateTime()).isEqualTo(diaryMeals.get(1).getDiaryMetaData().getDateTime());
        assertThat(diaryEntry.getMeals().get(1).getPortion()).isEqualTo(diaryMeals.get(1).getPortion());
        mealBuilderTest.assert_meal_isEqualTo_dummyCatalogueMeal2(diaryEntry.getMeals().get(1).getMeal());
    }
}
