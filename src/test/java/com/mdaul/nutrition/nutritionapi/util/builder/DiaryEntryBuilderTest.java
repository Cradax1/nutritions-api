package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.DiaryEntry;
import com.mdaul.nutrition.nutritionapi.api.model.Food;
import com.mdaul.nutrition.nutritionapi.api.model.Meal;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMeal;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryFood;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryMeal;
import com.mdaul.nutrition.nutritionapi.model.database.embedded.DiaryMetaData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DiaryEntryBuilderTest {

    private final FoodBuilder foodBuilder = mock(FoodBuilder.class);
    private final MealBuilder mealBuilder = mock(MealBuilder.class);
    private final DiaryEntryBuilder diaryEntryBuilder = new DiaryEntryBuilder(foodBuilder, mealBuilder);
    private static final String DUMMY_USER_ID = "809af8s90a8fd09a8sd";
    private final LocalDate dummyAssignedDay = LocalDate.of(2022, 9, 23);
    private final LocalDate dummyAssignedDay2 = LocalDate.of(2022, 10, 23);

    private final List<DiaryFood> dummyDiaryFood = List.of(DiaryFood.builder()
                    .id(982348928948293L)
                    .diaryMetaData(DiaryMetaData.builder()
                            .userId(DUMMY_USER_ID)
                            .assignedDay(dummyAssignedDay)
                            .dateTime(LocalDateTime.of(dummyAssignedDay, LocalTime.of(9, 32, 2)))
                            .build())
                    .catalogueUserFood(CatalogueUserFood.builder().name("food1").build())
                    .gram(94)
                    .build(),
            DiaryFood.builder()
                    .id(1232132138293L)
                    .diaryMetaData(DiaryMetaData.builder()
                            .userId(DUMMY_USER_ID)
                            .assignedDay(dummyAssignedDay)
                            .dateTime(LocalDateTime.of(dummyAssignedDay.plusDays(1), LocalTime.of(2, 1, 43)))
                            .build())
                    .catalogueUserFood(CatalogueUserFood.builder().name("food2").build())
                    .gram(87)
                    .build());

    private final List<DiaryFood> dummyDiaryFood2 = List.of(DiaryFood.builder()
            .id(839403894930933L)
            .diaryMetaData(DiaryMetaData.builder()
                    .userId(DUMMY_USER_ID)
                    .assignedDay(dummyAssignedDay2)
                    .dateTime(LocalDateTime.of(dummyAssignedDay2, LocalTime.of(9, 32, 2)))
                    .build())
            .catalogueUserFood(CatalogueUserFood.builder().name("food3").build())
            .gram(108)
            .build());

    private final List<DiaryMeal> dummyDiaryMeals = List.of(DiaryMeal.builder()
                    .id(121209099033L)
                    .diaryMetaData(DiaryMetaData.builder()
                            .userId(DUMMY_USER_ID)
                            .assignedDay(dummyAssignedDay)
                            .dateTime(LocalDateTime.of(dummyAssignedDay, LocalTime.of(12, 15, 41)))
                            .build())
                    .catalogueMeal(CatalogueMeal.builder().name("meal1").build())
                    .portion(new BigDecimal("1.6"))
                    .build(),
            DiaryMeal.builder()
                    .id(121209099033L)
                    .diaryMetaData(DiaryMetaData.builder()
                            .userId(DUMMY_USER_ID)
                            .assignedDay(dummyAssignedDay)
                            .dateTime(LocalDateTime.of(dummyAssignedDay.plusDays(1), LocalTime.of(0, 23, 51)))
                            .build())
                    .catalogueMeal(CatalogueMeal.builder().name("meal2").build())
                    .portion(new BigDecimal("0.8"))
                    .build());

    private final List<DiaryMeal> dummyDiaryMeals2 = List.of(DiaryMeal.builder()
            .id(238432409244324L)
            .diaryMetaData(DiaryMetaData.builder()
                    .userId(DUMMY_USER_ID)
                    .assignedDay(dummyAssignedDay2)
                    .dateTime(LocalDateTime.of(dummyAssignedDay2, LocalTime.of(12, 15, 41)))
                    .build())
            .catalogueMeal(CatalogueMeal.builder().name("meal3").build())
            .portion(new BigDecimal("2.5"))
            .build());

    @Test
    void build_1food() {
        List<DiaryFood> diaryFood = dummyDiaryFood2;
        when(foodBuilder.build(diaryFood.get(0).getCatalogueUserFood()))
                .thenReturn(new Food().name(diaryFood.get(0).getCatalogueUserFood().getName()));

        DiaryEntry diaryEntry = diaryEntryBuilder
                .build(diaryFood.get(0).getDiaryMetaData().getAssignedDay(), diaryFood, List.of());

        assertDiaryEntryEqualsDiaryFood(diaryEntry, diaryFood);
    }

    @Test
    void build_1meal() {
        List<DiaryMeal> diaryMeals = dummyDiaryMeals2;
        when(mealBuilder.build(diaryMeals.get(0).getCatalogueMeal()))
                .thenReturn(new Meal().name(diaryMeals.get(0).getCatalogueMeal().getName()));

        DiaryEntry diaryEntry = diaryEntryBuilder
                .build(diaryMeals.get(0).getDiaryMetaData().getAssignedDay(), List.of(), diaryMeals);

        assertDiaryEntryEqualsDiaryMeals(diaryEntry, diaryMeals);
    }

    @Test
    void build_2food2meals() {
        List<DiaryFood> diaryFood = dummyDiaryFood;
        List<DiaryMeal> diaryMeals = dummyDiaryMeals;
        when(foodBuilder.build(diaryFood.get(0).getCatalogueUserFood()))
                .thenReturn(new Food().name(diaryFood.get(0).getCatalogueUserFood().getName()));
        when(foodBuilder.build(diaryFood.get(1).getCatalogueUserFood()))
                .thenReturn(new Food().name(diaryFood.get(1).getCatalogueUserFood().getName()));
        when(mealBuilder.build(diaryMeals.get(0).getCatalogueMeal()))
                .thenReturn(new Meal().name(diaryMeals.get(0).getCatalogueMeal().getName()));
        when(mealBuilder.build(diaryMeals.get(1).getCatalogueMeal()))
                .thenReturn(new Meal().name(diaryMeals.get(1).getCatalogueMeal().getName()));

        DiaryEntry diaryEntry = diaryEntryBuilder.build(dummyAssignedDay, dummyDiaryFood, dummyDiaryMeals);

        assertDiaryEntryEqualsDiaryFood(diaryEntry, diaryFood);
        assertDiaryEntryEqualsDiaryMeals(diaryEntry, diaryMeals);
    }

    private void assertDiaryEntryEqualsDiaryFood(DiaryEntry diaryEntry, List<DiaryFood> diaryFoodList) {
        assertThat(diaryEntry.getFood()).hasSameSizeAs(diaryFoodList);
        for (DiaryFood diaryFood: diaryFoodList) {
            assertThat(diaryEntry.getAssignedDay()).isEqualTo(diaryFood.getDiaryMetaData().getAssignedDay());
        }
        for (int i = 0; i < diaryEntry.getFood().size(); i++) {
            assertThat(diaryEntry.getFood().get(i).getDateTime()).isEqualTo(diaryFoodList.get(i).getDiaryMetaData().getDateTime());
            assertThat(diaryEntry.getFood().get(i).getGram()).isEqualTo(diaryFoodList.get(i).getGram());
            assertThat(diaryEntry.getFood().get(i).getFood().getName()).isEqualTo(diaryFoodList.get(i).getCatalogueUserFood().getName());
        }
    }

    private void assertDiaryEntryEqualsDiaryMeals(DiaryEntry diaryEntry, List<DiaryMeal> diaryMeals) {
        assertThat(diaryEntry.getMeals()).hasSameSizeAs(diaryMeals);
        for (DiaryMeal diaryMeal: diaryMeals) {
            assertThat(diaryEntry.getAssignedDay()).isEqualTo(diaryMeal.getDiaryMetaData().getAssignedDay());
        }
        for (int i = 0; i < diaryEntry.getMeals().size(); i++) {
            assertThat(diaryEntry.getMeals().get(i).getDateTime()).isEqualTo(diaryMeals.get(i).getDiaryMetaData().getDateTime());
            assertThat(diaryEntry.getMeals().get(i).getPortion()).isEqualTo(diaryMeals.get(i).getPortion());
            assertThat(diaryEntry.getMeals().get(i).getMeal().getName()).isEqualTo(diaryMeals.get(i).getCatalogueMeal().getName());
        }
    }
}
