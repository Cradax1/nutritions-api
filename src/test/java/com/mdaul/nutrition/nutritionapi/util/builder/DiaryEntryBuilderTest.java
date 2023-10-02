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

    final LocalDate dummyAssignedDay = LocalDate.of(2022, 9, 23);
    final LocalDate dummyAssignedDay2 = LocalDate.of(2022, 10, 23);

    final List<DiaryFood> dummyDiaryFood = List.of(DiaryFood.builder()
                    .id(982348928948293L)
                    .diaryMetaData(DiaryMetaData.builder()
                            .userId(dummyCatalogueUserFoodInternal.getUserId())
                            .assignedDay(dummyAssignedDay)
                            .dateTime(LocalDateTime.of(dummyAssignedDay, LocalTime.of(9, 32, 2)))
                            .build())
                    .catalogueUserFood(dummyCatalogueUserFoodInternal)
                    .gram(94)
                    .build(),
            DiaryFood.builder()
                    .id(1232132138293L)
                    .diaryMetaData(DiaryMetaData.builder()
                            .userId(dummyCatalogueUserFoodInternal.getUserId())
                            .assignedDay(dummyAssignedDay)
                            .dateTime(LocalDateTime.of(dummyAssignedDay.plusDays(1), LocalTime.of(2, 1, 43)))
                            .build())
                    .catalogueUserFood(dummyCatalogueUserFoodExternal)
                    .gram(87)
                    .build());
    final List<DiaryFood> dummyDiaryFood2 = List.of(DiaryFood.builder()
            .id(839403894930933L)
            .diaryMetaData(DiaryMetaData.builder()
                    .userId(dummyCatalogueUserFoodInternal.getUserId())
                    .assignedDay(dummyAssignedDay2)
                    .dateTime(LocalDateTime.of(dummyAssignedDay2, LocalTime.of(9, 32, 2)))
                    .build())
            .catalogueUserFood(dummyCatalogueUserFoodInternal)
            .gram(108)
            .build());

    final List<DiaryMeal> dummyDiaryMeals = List.of(DiaryMeal.builder()
                    .id(121209099033L)
                    .diaryMetaData(DiaryMetaData.builder()
                            .userId(dummyCatalogueUserFoodInternal.getUserId())
                            .assignedDay(dummyAssignedDay)
                            .dateTime(LocalDateTime.of(dummyAssignedDay, LocalTime.of(12, 15, 41)))
                            .build())
                    .catalogueMeal(mealBuilderTest.dummyCatalogueMeal)
                    .portion(new BigDecimal("1.6"))
                    .build(),
            DiaryMeal.builder()
                    .id(121209099033L)
                    .diaryMetaData(DiaryMetaData.builder()
                            .userId(dummyCatalogueUserFoodInternal.getUserId())
                            .assignedDay(dummyAssignedDay)
                            .dateTime(LocalDateTime.of(dummyAssignedDay.plusDays(1), LocalTime.of(0, 23, 51)))
                            .build())
                    .catalogueMeal(mealBuilderTest.dummyCatalogueMeal2)
                    .portion(new BigDecimal("0.8"))
                    .build());

    final List<DiaryMeal> dummyDiaryMeals2 = List.of(DiaryMeal.builder()
            .id(238432409244324L)
            .diaryMetaData(DiaryMetaData.builder()
                    .userId(dummyCatalogueUserFoodInternal.getUserId())
                    .assignedDay(dummyAssignedDay2)
                    .dateTime(LocalDateTime.of(dummyAssignedDay2, LocalTime.of(12, 15, 41)))
                    .build())
            .catalogueMeal(mealBuilderTest.dummyCatalogueMeal)
            .portion(new BigDecimal("2.5"))
            .build());

    @Test
    void build_2food_2meals() {
        DiaryEntry diaryEntry = diaryEntryBuilder.build(dummyAssignedDay, dummyDiaryFood, dummyDiaryMeals);

        asset_diaryEntry_isEqualTo_dummyAssignedDay_dummyDiaryFood_dummyDiaryMeals(diaryEntry);
    }

    @Test
    void build_1food() {
        DiaryEntry diaryEntry = diaryEntryBuilder.build(dummyAssignedDay2, dummyDiaryFood2, List.of());

        asset_diaryEntry_isEqualTo_dummyAssignedDay2_dummyDiaryFood2(diaryEntry);
    }

    @Test
    void build_1meal() {
        DiaryEntry diaryEntry = diaryEntryBuilder.build(dummyAssignedDay2, List.of(), dummyDiaryMeals2);

        asset_diaryEntry_isEqualTo_dummyAssignedDay2_dummyDiaryMeals2(diaryEntry);
    }

    void asset_diaryEntry_isEqualTo_dummyAssignedDay_dummyDiaryFood_dummyDiaryMeals(DiaryEntry diaryEntry) {
        assertThat(diaryEntry.getAssignedDay()).isEqualTo(dummyAssignedDay);

        assertThat(diaryEntry.getFood().get(0).getGram()).isEqualTo(dummyDiaryFood.get(0).getGram());
        assertThat(diaryEntry.getFood().get(0).getDateTime()).isEqualTo(dummyDiaryFood.get(0).getDiaryMetaData().getDateTime());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodInternal(
                diaryEntry.getFood().get(0).getFood(), dummyDiaryFood.get(0).getCatalogueUserFood());

        assertThat(diaryEntry.getFood().get(1).getGram()).isEqualTo(dummyDiaryFood.get(1).getGram());
        assertThat(diaryEntry.getFood().get(1).getDateTime()).isEqualTo(dummyDiaryFood.get(1).getDiaryMetaData().getDateTime());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodExternal(
                diaryEntry.getFood().get(1).getFood(), dummyDiaryFood.get(1).getCatalogueUserFood());

        assertThat(diaryEntry.getMeals().get(0).getDateTime()).isEqualTo(dummyDiaryMeals.get(0).getDiaryMetaData().getDateTime());
        assertThat(diaryEntry.getMeals().get(0).getPortion()).isEqualTo(dummyDiaryMeals.get(0).getPortion());
        mealBuilderTest.assert_meal_isEqualTo_dummyCatalogueMeal(diaryEntry.getMeals().get(0).getMeal());

        assertThat(diaryEntry.getMeals().get(1).getDateTime()).isEqualTo(dummyDiaryMeals.get(1).getDiaryMetaData().getDateTime());
        assertThat(diaryEntry.getMeals().get(1).getPortion()).isEqualTo(dummyDiaryMeals.get(1).getPortion());
        mealBuilderTest.assert_meal_isEqualTo_dummyCatalogueMeal2(diaryEntry.getMeals().get(1).getMeal());
    }

    void asset_diaryEntry_isEqualTo_dummyAssignedDay_dummyDiaryFood(DiaryEntry diaryEntry) {
        assertThat(diaryEntry.getAssignedDay()).isEqualTo(dummyAssignedDay);

        assertThat(diaryEntry.getFood().get(0).getGram()).isEqualTo(dummyDiaryFood.get(0).getGram());
        assertThat(diaryEntry.getFood().get(0).getDateTime()).isEqualTo(dummyDiaryFood.get(0).getDiaryMetaData().getDateTime());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodInternal(
                diaryEntry.getFood().get(0).getFood(), dummyDiaryFood.get(0).getCatalogueUserFood());

        assertThat(diaryEntry.getFood().get(1).getGram()).isEqualTo(dummyDiaryFood.get(1).getGram());
        assertThat(diaryEntry.getFood().get(1).getDateTime()).isEqualTo(dummyDiaryFood.get(1).getDiaryMetaData().getDateTime());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodExternal(
                diaryEntry.getFood().get(1).getFood(), dummyDiaryFood.get(1).getCatalogueUserFood());
    }

    void asset_diaryEntry_isEqualTo_dummyAssignedDay_dummyDiaryMeals(DiaryEntry diaryEntry) {
        assertThat(diaryEntry.getAssignedDay()).isEqualTo(dummyAssignedDay);

        assertThat(diaryEntry.getMeals().get(0).getDateTime()).isEqualTo(dummyDiaryMeals.get(0).getDiaryMetaData().getDateTime());
        assertThat(diaryEntry.getMeals().get(0).getPortion()).isEqualTo(dummyDiaryMeals.get(0).getPortion());
        mealBuilderTest.assert_meal_isEqualTo_dummyCatalogueMeal(diaryEntry.getMeals().get(0).getMeal());

        assertThat(diaryEntry.getMeals().get(1).getDateTime()).isEqualTo(dummyDiaryMeals.get(1).getDiaryMetaData().getDateTime());
        assertThat(diaryEntry.getMeals().get(1).getPortion()).isEqualTo(dummyDiaryMeals.get(1).getPortion());
        mealBuilderTest.assert_meal_isEqualTo_dummyCatalogueMeal2(diaryEntry.getMeals().get(1).getMeal());
    }

    void asset_diaryEntry_isEqualTo_dummyAssignedDay2_dummyDiaryFood2_dummyDiaryMeals2(DiaryEntry diaryEntry) {
        assertThat(diaryEntry.getAssignedDay()).isEqualTo(dummyAssignedDay2);

        assertThat(diaryEntry.getFood().get(0).getGram()).isEqualTo(dummyDiaryFood2.get(0).getGram());
        assertThat(diaryEntry.getFood().get(0).getDateTime()).isEqualTo(dummyDiaryFood2.get(0).getDiaryMetaData().getDateTime());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodInternal(
                diaryEntry.getFood().get(0).getFood(), dummyDiaryFood2.get(0).getCatalogueUserFood());

        assertThat(diaryEntry.getMeals().get(0).getDateTime()).isEqualTo(dummyDiaryMeals2.get(0).getDiaryMetaData().getDateTime());
        assertThat(diaryEntry.getMeals().get(0).getPortion()).isEqualTo(dummyDiaryMeals2.get(0).getPortion());
        mealBuilderTest.assert_meal_isEqualTo_dummyCatalogueMeal(diaryEntry.getMeals().get(0).getMeal());
    }

    void asset_diaryEntry_isEqualTo_dummyAssignedDay2_dummyDiaryFood2(DiaryEntry diaryEntry) {
        assertThat(diaryEntry.getAssignedDay()).isEqualTo(dummyAssignedDay2);

        assertThat(diaryEntry.getFood().get(0).getGram()).isEqualTo(dummyDiaryFood2.get(0).getGram());
        assertThat(diaryEntry.getFood().get(0).getDateTime()).isEqualTo(dummyDiaryFood2.get(0).getDiaryMetaData().getDateTime());
        foodBuilderTest.assert_Food_isEqualTo_catalogueUserFoodInternal(
                diaryEntry.getFood().get(0).getFood(), dummyDiaryFood2.get(0).getCatalogueUserFood());
    }

    void asset_diaryEntry_isEqualTo_dummyAssignedDay2_dummyDiaryMeals2(DiaryEntry diaryEntry) {
        assertThat(diaryEntry.getAssignedDay()).isEqualTo(dummyAssignedDay2);

        assertThat(diaryEntry.getMeals().get(0).getDateTime()).isEqualTo(dummyDiaryMeals2.get(0).getDiaryMetaData().getDateTime());
        assertThat(diaryEntry.getMeals().get(0).getPortion()).isEqualTo(dummyDiaryMeals2.get(0).getPortion());
        mealBuilderTest.assert_meal_isEqualTo_dummyCatalogueMeal(diaryEntry.getMeals().get(0).getMeal());
    }
}
