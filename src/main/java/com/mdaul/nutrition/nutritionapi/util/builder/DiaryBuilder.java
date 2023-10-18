package com.mdaul.nutrition.nutritionapi.util.builder;

import com.mdaul.nutrition.nutritionapi.api.model.DiaryEntry;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryFood;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryMeal;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class DiaryBuilder {

    private final DiaryEntryBuilder diaryEntryBuilder;

    public DiaryBuilder(DiaryEntryBuilder diaryEntryBuilder) {
        this.diaryEntryBuilder = diaryEntryBuilder;
    }

    public List<DiaryEntry> build(List<DiaryFood> diaryFood, List<DiaryMeal> diaryMeal) {
        List<List<DiaryFood>> diaryFoodByDay = getDiaryFoodByDay(diaryFood);
        List<List<DiaryMeal>> diaryMealByDay = getDiaryMealByDay(diaryMeal);

        List<DiaryEntry> diary = new LinkedList<>();

        Iterator<List<DiaryMeal>> diaryMealByDayIt = diaryMealByDay.listIterator();
        List<DiaryMeal> diaryMealByDayCurrent = List.of();
        if (diaryMealByDayIt.hasNext()) {
            diaryMealByDayCurrent = diaryMealByDayIt.next();
        }
        Iterator<List<DiaryFood>> diaryFoodByDayIt = diaryFoodByDay.listIterator();
        List<DiaryFood> diaryFoodByDayCurrent = List.of();
        if (diaryFoodByDayIt.hasNext()) {
            diaryFoodByDayCurrent = diaryFoodByDayIt.next();
        }

        while (!diaryFoodByDayCurrent.isEmpty() || !diaryMealByDayCurrent.isEmpty()) {
            if (diaryMealByDayCurrent.isEmpty()) {
                diaryFoodByDayCurrent = addFoodDiaryEntry(diary, diaryFoodByDayCurrent, diaryFoodByDayIt);
            } else if (diaryFoodByDayCurrent.isEmpty()) {
                diaryMealByDayCurrent = addMealDiaryEntry(diary, diaryMealByDayCurrent, diaryMealByDayIt);
            } else if (diaryFoodByDayCurrent.get(0).getDiaryMetaData().getAssignedDay().compareTo(
                    diaryMealByDayCurrent.get(0).getDiaryMetaData().getAssignedDay()) > 0) {
                diaryFoodByDayCurrent = addFoodDiaryEntry(diary, diaryFoodByDayCurrent, diaryFoodByDayIt);
            } else if (diaryFoodByDayCurrent.get(0).getDiaryMetaData().getAssignedDay().compareTo(
                    diaryMealByDayCurrent.get(0).getDiaryMetaData().getAssignedDay()) == 0){
                addFoodAndMealEntry(diary, diaryFoodByDayCurrent, diaryMealByDayCurrent);
                diaryFoodByDayCurrent = getNextDiaryFood(diaryFoodByDayIt);
                diaryMealByDayCurrent = getNextDiaryMeal(diaryMealByDayIt);
            } else if (diaryFoodByDayCurrent.get(0).getDiaryMetaData().getAssignedDay().compareTo(
                    diaryMealByDayCurrent.get(0).getDiaryMetaData().getAssignedDay()) < 0) {
                diaryMealByDayCurrent = addMealDiaryEntry(diary, diaryMealByDayCurrent, diaryMealByDayIt);
            }
        }

        return diary;
    }

    private List<List<DiaryFood>> getDiaryFoodByDay(List<DiaryFood> diaryFood) {
        List<List<DiaryFood>> diaryFoodByDay = new LinkedList<>();
        LinkedList<DiaryFood> diaryFoodDay = new LinkedList<>();

        for (DiaryFood diaryFoodEntry : diaryFood) {
            if (diaryFoodDay.isEmpty()) {
                diaryFoodDay.add(diaryFoodEntry);
            } else if (diaryFoodEntry.getDiaryMetaData().getAssignedDay().equals(
                    diaryFoodDay.getLast().getDiaryMetaData().getAssignedDay())) {
                diaryFoodDay.add(diaryFoodEntry);
            } else {
                diaryFoodByDay.add(diaryFoodDay);
                diaryFoodDay = new LinkedList<>();
                diaryFoodDay.add(diaryFoodEntry);
            }
        }
        if (!diaryFoodDay.isEmpty()) {
            diaryFoodByDay.add(diaryFoodDay);
        }

        return diaryFoodByDay;
    }

    private List<List<DiaryMeal>> getDiaryMealByDay(List<DiaryMeal> diaryMeal) {
        List<List<DiaryMeal>> diaryMealByDay = new LinkedList<>();
        LinkedList<DiaryMeal> diaryMealDay = new LinkedList<>();

        for (DiaryMeal diaryMealEntry : diaryMeal) {
            if (diaryMealDay.isEmpty()) {
                diaryMealDay.add(diaryMealEntry);
            } else if (diaryMealEntry.getDiaryMetaData().getAssignedDay().equals(
                    diaryMealDay.getLast().getDiaryMetaData().getAssignedDay())) {
                diaryMealDay.add(diaryMealEntry);
            } else {
                diaryMealByDay.add(diaryMealDay);
                diaryMealDay = new LinkedList<>();
                diaryMealDay.add(diaryMealEntry);
            }
        }
        if (!diaryMealDay.isEmpty()) {
            diaryMealByDay.add(diaryMealDay);
        }

        return diaryMealByDay;
    }

    private void addFoodAndMealEntry(List<DiaryEntry> diary, List<DiaryFood> diaryFood, List<DiaryMeal> diaryMeals) {
        diary.add(diaryEntryBuilder.build(
                diaryFood.get(0).getDiaryMetaData().getAssignedDay(), diaryFood, diaryMeals));
    }

    private List<DiaryFood> addFoodDiaryEntry(List<DiaryEntry> diary, List<DiaryFood> diaryFood, Iterator<List<DiaryFood>> diaryFoodIt) {
        diary.add(diaryEntryBuilder.build(
                diaryFood.get(0).getDiaryMetaData().getAssignedDay(), diaryFood, List.of()));
        return getNextDiaryFood(diaryFoodIt);
    }

    private List<DiaryFood> getNextDiaryFood(Iterator<List<DiaryFood>> diaryFoodIt) {
        if (diaryFoodIt.hasNext()) {
            return diaryFoodIt.next();
        } else {
            return List.of();
        }
    }

    private List<DiaryMeal> addMealDiaryEntry(List<DiaryEntry> diary, List<DiaryMeal> diaryMeal, Iterator<List<DiaryMeal>> diaryMealIt) {
        diary.add(diaryEntryBuilder.build(
                diaryMeal.get(0).getDiaryMetaData().getAssignedDay(), List.of(), diaryMeal));
        return getNextDiaryMeal(diaryMealIt);
    }

    private List<DiaryMeal> getNextDiaryMeal(Iterator<List<DiaryMeal>> diaryMealIt) {
        if (diaryMealIt.hasNext()) {
            return diaryMealIt.next();
        } else {
            return List.of();
        }
    }
}