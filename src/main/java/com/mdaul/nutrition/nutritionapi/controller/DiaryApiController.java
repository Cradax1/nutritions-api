package com.mdaul.nutrition.nutritionapi.controller;

import com.mdaul.nutrition.nutritionapi.api.DiaryApi;
import com.mdaul.nutrition.nutritionapi.api.model.*;
import com.mdaul.nutrition.nutritionapi.service.DiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class DiaryApiController implements ApiController, DiaryApi {

    private final DiaryService diaryService;

    public DiaryApiController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @Override
    public ResponseEntity<DiaryEntry> diaryDayGet(LocalDate assignedDay) {
        return ResponseEntity.of(diaryService.getDiaryEntry(getUserId(), assignedDay));
    }

    @Override
    public ResponseEntity<List<DiaryEntry>> diaryListGet() {
        return ResponseEntity.of(diaryService.getDiary(getUserId()));
    }

    @Override
    public ResponseEntity<DiaryEntry> diaryFoodDayPost(LocalDate assignedDay, DiaryFoodEntrySubmission diaryFoodEntrySubmission) {
        Optional<DiaryEntry> diaryEntry = diaryService.saveDiaryFood(getUserId(), assignedDay, diaryFoodEntrySubmission);
        return diaryEntry.map(value
                -> new ResponseEntity<>(value, HttpStatus.CREATED)).orElseGet(()
                -> ResponseEntity.of(diaryEntry));
    }

    @Override
    public ResponseEntity<DiaryEntry> diaryFoodDayPut(LocalDate assignedDay, DiaryFoodEntrySubmission diaryFoodEntrySubmission) {
        return ResponseEntity.of(diaryService.updateDiaryFood(getUserId(), assignedDay, diaryFoodEntrySubmission));
    }

    @Override
    public ResponseEntity<DiaryEntry> diaryFoodDayDelete(LocalDate assignedDay, DiaryFoodEntryDeleteSubmission diaryFoodEntryDeleteSubmission) {
        return ResponseEntity.of(diaryService.deleteDiaryFood(getUserId(), assignedDay, diaryFoodEntryDeleteSubmission));
    }

    @Override
    public ResponseEntity<DiaryEntry> diaryMealDayPost(LocalDate assignedDay, DiaryMealEntrySubmission diaryMealEntrySubmission) {
        Optional<DiaryEntry> diaryEntry = diaryService.saveDiaryMeal(getUserId(), assignedDay, diaryMealEntrySubmission);
        return diaryEntry.map(value
                -> new ResponseEntity<>(value, HttpStatus.CREATED)).orElseGet(()
                -> ResponseEntity.of(diaryEntry));
    }

    @Override
    public ResponseEntity<DiaryEntry> diaryMealDayPut(LocalDate assignedDay, DiaryMealEntrySubmission diaryMealEntrySubmission) {
        return ResponseEntity.of(diaryService.updateDiaryMeal(getUserId(), assignedDay, diaryMealEntrySubmission));
    }

    @Override
    public ResponseEntity<DiaryEntry> diaryMealDayDelete(LocalDate assignedDay, DiaryMealEntryDeleteSubmission diaryMealEntryDeleteSubmission) {
        return ResponseEntity.of(diaryService.deleteDiaryMeal(getUserId(), assignedDay, diaryMealEntryDeleteSubmission));
    }
}
