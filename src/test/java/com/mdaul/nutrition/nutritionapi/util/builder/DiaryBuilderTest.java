package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.*;
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

class DiaryBuilderTest {

    private final DiaryEntryBuilder diaryEntryBuilder = mock(DiaryEntryBuilder.class);
    private final DiaryBuilder diaryBuilder = new DiaryBuilder(diaryEntryBuilder);
    private static final String DUMMY_USER_ID = "809af8s90a8fd09a8sd";
    private final LocalDate dummyAssignedDay = LocalDate.of(2022, 9, 23);
    private final LocalDate dummyAssignedDay2 = LocalDate.of(2022, 10, 23);
    //same day as one but covers testcase in which localDate is compared by poor == instead of isEqualTo()
    private final LocalDate dummyAssignedDay3 = LocalDate.of(2022, 9, 23);

    private final DiaryFood dummyDiaryFood = DiaryFood.builder()
            .id(839403894930933L)
            .diaryMetaData(DiaryMetaData.builder()
                    .userId(DUMMY_USER_ID)
                    .assignedDay(dummyAssignedDay)
                    .dateTime(LocalDateTime.of(dummyAssignedDay, LocalTime.of(9, 32, 2)))
                    .build())
            .catalogueUserFood(CatalogueUserFood.builder().name("food1").build())
            .gram(108)
            .build();

    private final DiaryFood dummyDiaryFood2 = DiaryFood.builder()
            .id(1232132138293L)
            .diaryMetaData(DiaryMetaData.builder()
                    .userId(DUMMY_USER_ID)
                    .assignedDay(dummyAssignedDay2)
                    .dateTime(LocalDateTime.of(dummyAssignedDay2.plusDays(1), LocalTime.of(2, 1, 43)))
                    .build())
            .catalogueUserFood(CatalogueUserFood.builder().name("food2").build())
            .gram(87)
            .build();

    private final DiaryFood dummyDiaryFood3 = DiaryFood.builder()
            .id(839403894930933L)
            .diaryMetaData(DiaryMetaData.builder()
                    .userId(DUMMY_USER_ID)
                    .assignedDay(dummyAssignedDay3)
                    .dateTime(LocalDateTime.of(dummyAssignedDay3, LocalTime.of(10, 1, 58)))
                    .build())
            .catalogueUserFood(CatalogueUserFood.builder().name("food3").build())
            .gram(152)
            .build();

    private final DiaryMeal dummyDiaryMeal = DiaryMeal.builder()
            .id(238432409244324L)
            .diaryMetaData(DiaryMetaData.builder()
                    .userId(DUMMY_USER_ID)
                    .assignedDay(dummyAssignedDay)
                    .dateTime(LocalDateTime.of(dummyAssignedDay, LocalTime.of(12, 15, 41)))
                    .build())
            .catalogueMeal(CatalogueMeal.builder().name("meal1").build())
            .portion(new BigDecimal("2.5"))
            .build();

    private final DiaryMeal dummyDiaryMeal2 = DiaryMeal.builder()
            .id(121209099033L)
            .diaryMetaData(DiaryMetaData.builder()
                    .userId(DUMMY_USER_ID)
                    .assignedDay(dummyAssignedDay2)
                    .dateTime(LocalDateTime.of(dummyAssignedDay2.plusDays(1), LocalTime.of(0, 23, 51)))
                    .build())
            .catalogueMeal(CatalogueMeal.builder().name("meal2").build())
            .portion(new BigDecimal("0.8"))
            .build();

    private final DiaryMeal dummyDiaryMeal3 = DiaryMeal.builder()
            .id(238432409244324L)
            .diaryMetaData(DiaryMetaData.builder()
                    .userId(DUMMY_USER_ID)
                    .assignedDay(dummyAssignedDay3)
                    .dateTime(LocalDateTime.of(dummyAssignedDay3, LocalTime.of(18, 10, 31)))
                    .build())
            .catalogueMeal(CatalogueMeal.builder().name("meal3").build())
            .portion(new BigDecimal("2.9"))
            .build();

    @Test
    void build_1day1food() {
        List<DiaryFood> diaryFood = List.of(dummyDiaryFood);
        when(diaryEntryBuilder.build(diaryFood.get(0).getDiaryMetaData().getAssignedDay(), diaryFood, List.of()))
                .thenReturn(new DiaryEntry()
                        .assignedDay(diaryFood.get(0).getDiaryMetaData().getAssignedDay())
                        .food(List.of(new DiaryFoodEntry()
                                .dateTime(diaryFood.get(0).getDiaryMetaData().getDateTime())
                                .gram(diaryFood.get(0).getGram())
                                .food(new Food().name(diaryFood.get(0).getCatalogueUserFood().getName())))));

        List<DiaryEntry> diary = diaryBuilder.build(diaryFood, List.of());

        assertThat(diary).hasSize(1);
        assertEquals_DiaryEntry_DiaryFood(diary.get(0), diaryFood);
    }

    @Test
    void build_1day1meal() {
        List<DiaryMeal> diaryMeals = List.of(dummyDiaryMeal);
        when(diaryEntryBuilder.build(diaryMeals.get(0).getDiaryMetaData().getAssignedDay(), List.of(), diaryMeals))
                .thenReturn(new DiaryEntry()
                        .assignedDay(diaryMeals.get(0).getDiaryMetaData().getAssignedDay())
                        .meals(List.of(new DiaryMealEntry()
                                .dateTime(diaryMeals.get(0).getDiaryMetaData().getDateTime())
                                .portion(diaryMeals.get(0).getPortion())
                                .meal(new Meal().name(diaryMeals.get(0).getCatalogueMeal().getName())))));

        List<DiaryEntry> diary = diaryBuilder.build(List.of(), diaryMeals);

        assertThat(diary).hasSize(1);
        assertEquals_DiaryEntry_DiaryMeal(diary.get(0), diaryMeals);
    }

    @Test
    void build_1day2food2meals() {
        List<DiaryFood> diaryFood = List.of(dummyDiaryFood, dummyDiaryFood3);
        List<DiaryMeal> diaryMeals = List.of(dummyDiaryMeal, dummyDiaryMeal3);
        when(diaryEntryBuilder.build(diaryFood.get(0).getDiaryMetaData().getAssignedDay(), diaryFood, diaryMeals))
                .thenReturn(new DiaryEntry()
                        .assignedDay(diaryFood.get(0).getDiaryMetaData().getAssignedDay())
                        .food(List.of(
                                new DiaryFoodEntry()
                                        .dateTime(diaryFood.get(0).getDiaryMetaData().getDateTime())
                                        .gram(diaryFood.get(0).getGram())
                                        .food(new Food().name(diaryFood.get(0).getCatalogueUserFood().getName())),
                                new DiaryFoodEntry()
                                        .dateTime(diaryFood.get(1).getDiaryMetaData().getDateTime())
                                        .gram(diaryFood.get(1).getGram())
                                        .food(new Food().name(diaryFood.get(1).getCatalogueUserFood().getName()))))
                        .meals(List.of(
                                new DiaryMealEntry()
                                        .dateTime(diaryMeals.get(0).getDiaryMetaData().getDateTime())
                                        .portion(diaryMeals.get(0).getPortion())
                                        .meal(new Meal().name(diaryMeals.get(0).getCatalogueMeal().getName())),
                                new DiaryMealEntry()
                                        .dateTime(diaryMeals.get(1).getDiaryMetaData().getDateTime())
                                        .portion(diaryMeals.get(1).getPortion())
                                        .meal(new Meal().name(diaryMeals.get(1).getCatalogueMeal().getName())))));

        List<DiaryEntry> diary = diaryBuilder.build(diaryFood, diaryMeals);

        assertThat(diary).hasSize(1);
        assertEquals_DiaryEntry_DiaryFood(diary.get(0), diaryFood);
        assertEquals_DiaryEntry_DiaryMeal(diary.get(0), diaryMeals);
    }

    @Test
    void build_2dayFirstMealSecondFood() {
        List<DiaryFood> diaryFood = List.of(dummyDiaryFood2);
        List<DiaryMeal> diaryMeals = List.of(dummyDiaryMeal);
        when(diaryEntryBuilder.build(diaryFood.get(0).getDiaryMetaData().getAssignedDay(), diaryFood, List.of()))
                .thenReturn(new DiaryEntry()
                        .assignedDay(diaryFood.get(0).getDiaryMetaData().getAssignedDay())
                        .food(List.of(new DiaryFoodEntry()
                                .dateTime(diaryFood.get(0).getDiaryMetaData().getDateTime())
                                .gram(diaryFood.get(0).getGram())
                                .food(new Food().name(diaryFood.get(0).getCatalogueUserFood().getName())))));
        when(diaryEntryBuilder.build(diaryMeals.get(0).getDiaryMetaData().getAssignedDay(), List.of(), diaryMeals))
                .thenReturn(new DiaryEntry()
                        .assignedDay(diaryMeals.get(0).getDiaryMetaData().getAssignedDay())
                        .meals(List.of(new DiaryMealEntry()
                                .dateTime(diaryMeals.get(0).getDiaryMetaData().getDateTime())
                                .portion(diaryMeals.get(0).getPortion())
                                .meal(new Meal().name(diaryMeals.get(0).getCatalogueMeal().getName())))));

        List<DiaryEntry> diary = diaryBuilder.build(diaryFood, diaryMeals);

        assertThat(diary).hasSize(2);
        assertEquals_DiaryEntry_DiaryFood(diary.get(0), diaryFood);
        assertEquals_DiaryEntry_DiaryMeal(diary.get(1), diaryMeals);
    }

    @Test
    void build_2dayFirstFoodSecondMeal() {
        List<DiaryFood> diaryFood = List.of(dummyDiaryFood);
        List<DiaryMeal> diaryMeals = List.of(dummyDiaryMeal2);
        when(diaryEntryBuilder.build(diaryFood.get(0).getDiaryMetaData().getAssignedDay(), diaryFood, List.of()))
                .thenReturn(new DiaryEntry()
                        .assignedDay(diaryFood.get(0).getDiaryMetaData().getAssignedDay())
                        .food(List.of(new DiaryFoodEntry()
                                .dateTime(diaryFood.get(0).getDiaryMetaData().getDateTime())
                                .gram(diaryFood.get(0).getGram())
                                .food(new Food().name(diaryFood.get(0).getCatalogueUserFood().getName())))));
        when(diaryEntryBuilder.build(diaryMeals.get(0).getDiaryMetaData().getAssignedDay(), List.of(), diaryMeals))
                .thenReturn(new DiaryEntry()
                        .assignedDay(diaryMeals.get(0).getDiaryMetaData().getAssignedDay())
                        .meals(List.of(new DiaryMealEntry()
                                .dateTime(diaryMeals.get(0).getDiaryMetaData().getDateTime())
                                .portion(diaryMeals.get(0).getPortion())
                                .meal(new Meal().name(diaryMeals.get(0).getCatalogueMeal().getName())))));

        List<DiaryEntry> diary = diaryBuilder.build(diaryFood, diaryMeals);

        assertThat(diary).hasSize(2);
        assertEquals_DiaryEntry_DiaryMeal(diary.get(0), diaryMeals);
        assertEquals_DiaryEntry_DiaryFood(diary.get(1), diaryFood);
    }

    @Test
    void build_2daysFoodMealEachDay() {
        List<DiaryFood> diaryFood = List.of(dummyDiaryFood2, dummyDiaryFood);
        List<DiaryMeal> diaryMeals = List.of(dummyDiaryMeal2, dummyDiaryMeal);
        when(diaryEntryBuilder.build(diaryFood.get(0).getDiaryMetaData().getAssignedDay(), List.of(diaryFood.get(0)),
                List.of(diaryMeals.get(0))))
                .thenReturn(new DiaryEntry()
                        .assignedDay(diaryFood.get(0).getDiaryMetaData().getAssignedDay())
                        .food(List.of(new DiaryFoodEntry()
                                .dateTime(diaryFood.get(0).getDiaryMetaData().getDateTime())
                                .gram(diaryFood.get(0).getGram())
                                .food(new Food().name(diaryFood.get(0).getCatalogueUserFood().getName()))))
                        .meals(List.of(new DiaryMealEntry()
                                .dateTime(diaryMeals.get(0).getDiaryMetaData().getDateTime())
                                .portion(diaryMeals.get(0).getPortion())
                                .meal(new Meal().name(diaryMeals.get(0).getCatalogueMeal().getName())))));
        when(diaryEntryBuilder.build(diaryFood.get(1).getDiaryMetaData().getAssignedDay(), List.of(diaryFood.get(1)),
                List.of(diaryMeals.get(1))))
                .thenReturn(new DiaryEntry()
                        .assignedDay(diaryFood.get(1).getDiaryMetaData().getAssignedDay())
                        .food(List.of(new DiaryFoodEntry()
                                .dateTime(diaryFood.get(1).getDiaryMetaData().getDateTime())
                                .gram(diaryFood.get(1).getGram())
                                .food(new Food().name(diaryFood.get(1).getCatalogueUserFood().getName()))))
                        .meals(List.of(new DiaryMealEntry()
                                .dateTime(diaryMeals.get(1).getDiaryMetaData().getDateTime())
                                .portion(diaryMeals.get(1).getPortion())
                                .meal(new Meal().name(diaryMeals.get(1).getCatalogueMeal().getName())))));

        List<DiaryEntry> diary = diaryBuilder.build(diaryFood, diaryMeals);

        assertThat(diary).hasSize(2);
        assertEquals_DiaryEntry_DiaryFood(diary.get(0), List.of(dummyDiaryFood2));
        assertEquals_DiaryEntry_DiaryMeal(diary.get(0), List.of(dummyDiaryMeal2));
        assertEquals_DiaryEntry_DiaryFood(diary.get(1), List.of(dummyDiaryFood));
        assertEquals_DiaryEntry_DiaryMeal(diary.get(1), List.of(dummyDiaryMeal));
    }

    private void assertEquals_DiaryEntry_DiaryFood(DiaryEntry diaryEntry, List<DiaryFood> diaryFoodList) {
        assertThat(diaryEntry.getFood()).hasSameSizeAs(diaryFoodList);
        for (DiaryFood diaryFood : diaryFoodList) {
            assertThat(diaryEntry.getAssignedDay()).isEqualTo(diaryFood.getDiaryMetaData().getAssignedDay());
        }
        for (int i = 0; i < diaryFoodList.size(); i++) {
            assertThat(diaryEntry.getFood().get(i).getDateTime())
                    .isEqualTo(diaryFoodList.get(i).getDiaryMetaData().getDateTime());
            assertThat(diaryEntry.getFood().get(i).getGram()).isEqualTo(diaryFoodList.get(i).getGram());
            assertThat(diaryEntry.getFood().get(i).getFood().getName())
                    .isEqualTo(diaryFoodList.get(i).getCatalogueUserFood().getName());
        }
    }

    private void assertEquals_DiaryEntry_DiaryMeal(DiaryEntry diaryEntry, List<DiaryMeal> diaryMeals) {
        assertThat(diaryEntry.getMeals()).hasSameSizeAs(diaryMeals);
        for (DiaryMeal diaryMeal : diaryMeals) {
            assertThat(diaryEntry.getAssignedDay()).isEqualTo(diaryMeal.getDiaryMetaData().getAssignedDay());
        }
        for (int i = 0; i < diaryMeals.size(); i++) {
            assertThat(diaryEntry.getMeals().get(i).getDateTime()).isEqualTo(diaryMeals.get(i).getDiaryMetaData().getDateTime());
            assertThat(diaryEntry.getMeals().get(i).getPortion()).isEqualTo(diaryMeals.get(i).getPortion());
            assertThat(diaryEntry.getMeals().get(i).getMeal().getName()).isEqualTo(diaryMeals.get(i).getCatalogueMeal().getName());
        }
    }
}