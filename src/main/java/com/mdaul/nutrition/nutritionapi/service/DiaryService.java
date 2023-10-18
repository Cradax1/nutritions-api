package com.mdaul.nutrition.nutritionapi.service;

import com.mdaul.nutrition.nutritionapi.api.model.*;
import com.mdaul.nutrition.nutritionapi.exception.DataIntegrityException;
import com.mdaul.nutrition.nutritionapi.exception.DatabaseEntryAlreadyExistingException;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMeal;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryFood;
import com.mdaul.nutrition.nutritionapi.model.database.DiaryMeal;
import com.mdaul.nutrition.nutritionapi.repository.CatalogueMealRepository;
import com.mdaul.nutrition.nutritionapi.repository.CatalogueUserFoodRepository;
import com.mdaul.nutrition.nutritionapi.repository.DiaryFoodRepository;
import com.mdaul.nutrition.nutritionapi.repository.DiaryMealRepository;
import com.mdaul.nutrition.nutritionapi.util.DataEntriesOptimizer;
import com.mdaul.nutrition.nutritionapi.util.builder.DiaryBuilder;
import com.mdaul.nutrition.nutritionapi.util.builder.DiaryEntryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DiaryService {
    private final DiaryEntryBuilder diaryEntryBuilder;
    private final DiaryBuilder diaryBuilder;
    private final DataEntriesOptimizer dataEntriesOptimizer;
    @Autowired
    private CatalogueUserFoodRepository catalogueUserFoodRepository;
    @Autowired
    private CatalogueMealRepository catalogueMealRepository;
    @Autowired
    private DiaryFoodRepository diaryFoodRepository;
    @Autowired
    private DiaryMealRepository diaryMealRepository;

    public DiaryService(DiaryEntryBuilder diaryEntryBuilder, DiaryBuilder diaryBuilder,
                        DataEntriesOptimizer dataEntriesOptimizer) {
        this.diaryEntryBuilder = diaryEntryBuilder;
        this.diaryBuilder = diaryBuilder;
        this.dataEntriesOptimizer = dataEntriesOptimizer;
    }

    @Transactional
    public Optional<DiaryEntry> deleteDiaryMeal(String userId, LocalDate assignedDay,
                                                DiaryMealEntryDeleteSubmission diaryMealEntryDeleteSubmission) {
        log.info("Deleting diary meal entry with name {}, assignedDay {}, date-time {} and user with id {}",
                diaryMealEntryDeleteSubmission.getName(), assignedDay,
                diaryMealEntryDeleteSubmission.getDateTime(), userId);
        Optional<DiaryMeal> diaryMeal = diaryMealRepository
                .findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDayAndDiaryMetaDataDateTimeAndCatalogueMealName(
                        userId, assignedDay, diaryMealEntryDeleteSubmission.getDateTime(),
                        diaryMealEntryDeleteSubmission.getName());
        if (diaryMeal.isEmpty()) {
            log.debug("Could not find diary meal entry with name {}, assignedDay {}, date-time {} and user with id {}",
                    diaryMealEntryDeleteSubmission.getName(), assignedDay,
                    diaryMealEntryDeleteSubmission.getDateTime(), userId);
            return Optional.empty();
        }
        diaryMealRepository.delete(diaryMeal.get());
        dataEntriesOptimizer.optimizeByCatalogueMealId(diaryMeal.get().getCatalogueMeal().getId());
        return getDiaryEntry(userId, assignedDay);
    }

    @Transactional
    public Optional<DiaryEntry> deleteDiaryFood(String userId, LocalDate assignedDay,
                                                DiaryFoodEntryDeleteSubmission diaryFoodEntryDeleteSubmission) {
        log.info("Deleting diary food entry with name {}, assignedDay {}, date-time {} and user with id {}",
                diaryFoodEntryDeleteSubmission.getName(), assignedDay,
                diaryFoodEntryDeleteSubmission.getDateTime(), userId);
        Optional<DiaryFood> diaryFood = diaryFoodRepository
                .findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDayAndDiaryMetaDataDateTimeAndCatalogueUserFoodName(
                        userId, assignedDay, diaryFoodEntryDeleteSubmission.getDateTime(),
                        diaryFoodEntryDeleteSubmission.getName());
        if (diaryFood.isEmpty()) {
            log.debug("Could not find diary food entry with name {}, assignedDay {}, date-time {} and user with id {}",
                    diaryFoodEntryDeleteSubmission.getName(), assignedDay,
                    diaryFoodEntryDeleteSubmission.getDateTime(), userId);
            return Optional.empty();
        }
        diaryFoodRepository.delete(diaryFood.get());
        dataEntriesOptimizer.optimizeByCatalogueUserFoodId(diaryFood.get().getCatalogueUserFood().getId());
        return getDiaryEntry(userId, assignedDay);
    }

    @Transactional
    public Optional<DiaryEntry> updateDiaryFood(String userId, LocalDate assignedDay,
                                                DiaryFoodEntrySubmission diaryFoodEntrySubmission) {
        log.info("Updating diary food entry with name {}, assignedDay {}, date-time{} and user with id {}",
                diaryFoodEntrySubmission.getName(), assignedDay, diaryFoodEntrySubmission.getDateTime(), userId);
        Optional<DiaryFood> diaryFood = diaryFoodRepository
                .findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDayAndCatalogueUserFoodName(
                        userId, assignedDay, diaryFoodEntrySubmission.getName());
        if (diaryFood.isEmpty()) {
            log.debug("DiaryFood with name {}, date-time {} and userId {} not found",
                    diaryFoodEntrySubmission.getName(), diaryFoodEntrySubmission.getDateTime(), userId);
            return Optional.empty();
        }
        diaryFoodRepository.setGramById(diaryFoodEntrySubmission.getGram(), diaryFood.get().getId());
        return getDiaryEntry(userId, assignedDay);
    }

    @Transactional
    public Optional<DiaryEntry> updateDiaryMeal(String userId, LocalDate assignedDay,
                                                DiaryMealEntrySubmission diaryMealEntrySubmission) {
        log.info("Updating diary meal entry with name {}, with assignedDay {} and user with id {}",
                diaryMealEntrySubmission.getName(), assignedDay, userId);
        Optional<DiaryMeal> diaryMeal = diaryMealRepository
                .findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDayAndCatalogueMealName(
                        userId, assignedDay, diaryMealEntrySubmission.getName());
        if (diaryMeal.isEmpty()) {
            log.debug("DiaryMeal with name {}, date-time {} and userId {} not found",
                    diaryMealEntrySubmission.getName(), diaryMealEntrySubmission.getDateTime(), userId);
            return Optional.empty();
        }
        diaryMealRepository.setPortionById(diaryMealEntrySubmission.getPortion(), diaryMeal.get().getId());
        return getDiaryEntry(userId, assignedDay);
    }

    public Optional<List<DiaryEntry>> getDiary(String userId) {
        log.info("Trying to find all diary entries of user with id {}", userId);
        List<DiaryFood> diaryFood =
                diaryFoodRepository.findByDiaryMetaDataUserIdOrderByDiaryMetaDataAssignedDayDesc(userId);
        addCatalogueUserFood(diaryFood);
        List<DiaryMeal> diaryMeals =
                diaryMealRepository.findByDiaryMetaDataUserIdOrderByDiaryMetaDataAssignedDayDesc(userId);
        addCatalogueMeal(diaryMeals);
        return Optional.of(diaryBuilder.build(diaryFood, diaryMeals));
    }

    public Optional<DiaryEntry> getDiaryEntry(String userId, LocalDate assignedDay) {
        log.info("Trying to find diary entry with assignedDay {} and user with id {}", assignedDay, userId);
        List<DiaryFood> diaryFood =
                diaryFoodRepository.findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDay(userId, assignedDay);
        addCatalogueUserFood(diaryFood);
        List<DiaryMeal> diaryMeal =
                diaryMealRepository.findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDay(userId, assignedDay);
        addCatalogueMeal(diaryMeal);
        return Optional.of(diaryEntryBuilder.build(assignedDay, diaryFood, diaryMeal));
    }

    private void addCatalogueUserFood(List<DiaryFood> diaryFood) {
        for (DiaryFood diaryFoodItem : diaryFood) {
            Optional<CatalogueUserFood> catalogueUserFoodResult =
                    catalogueUserFoodRepository.findById(diaryFoodItem.getCatalogueUserFood().getId());
            if (catalogueUserFoodResult.isEmpty()) {
                String message = String.format("Could not find CatalogueUserFood of existing DiaryFood with id %d",
                        diaryFoodItem.getId());
                throw new DataIntegrityException(message);
            }
            diaryFoodItem.setCatalogueUserFood(catalogueUserFoodResult.get());
        }
    }

    private void addCatalogueMeal(List<DiaryMeal> diaryMeal) {
        for (DiaryMeal diaryMealItem : diaryMeal) {
            Optional<CatalogueMeal> catalogueMeal =
                    catalogueMealRepository.findById(diaryMealItem.getCatalogueMeal().getId());
            if (catalogueMeal.isEmpty()) {
                String message = String.format("Could not find CatalogueMeal of existing DiaryMeal with id %d",
                        diaryMealItem.getId());
                throw new DataIntegrityException(message);
            }
            diaryMealItem.setCatalogueMeal(catalogueMeal.get());
            diaryMealItem.setPortion(diaryMealItem.getPortion());
        }
    }

    public Optional<DiaryEntry> saveDiaryFood(String userId, LocalDate assignedDay,
                                              DiaryFoodEntrySubmission diaryFoodEntrySubmission) {
        log.info("Trying to save diary food entry with assignedDay {} and user with id {}", assignedDay, userId);
        DiaryFood diaryFood = getActiveDiaryFood(userId, assignedDay, diaryFoodEntrySubmission);
        if (diaryFood == null) {
            return Optional.empty();
        }
        if (diaryFoodExists(diaryFood.getDiaryMetaData().getUserId(), diaryFood.getDiaryMetaData().getAssignedDay(),
                diaryFood.getDiaryMetaData().getDateTime(), diaryFood.getCatalogueUserFood().getName())) {
            String message = String.format("diary food entry with name %s, assignedDay %s, date-time %s and user" +
                            " with id %s already exists",
                    diaryFood.getCatalogueUserFood().getName(), diaryFood.getDiaryMetaData().getAssignedDay(),
                    diaryFood.getDiaryMetaData().getDateTime(), diaryFood.getDiaryMetaData().getUserId());
            throw new DatabaseEntryAlreadyExistingException(message);
        }
        return saveDiaryFoodDo(userId, assignedDay, diaryFood);
    }

    private boolean diaryFoodExists(String userId, LocalDate assignedDay, LocalDateTime dateTime, String name) {
        return diaryFoodRepository
                .findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDayAndDiaryMetaDataDateTimeAndCatalogueUserFoodName(
                        userId, assignedDay, dateTime, name)
                .isPresent();
    }

    private Optional<DiaryEntry> saveDiaryFoodDo(String userId, LocalDate assignedDay, DiaryFood diaryFood) {
        diaryFoodRepository.save(diaryFood);
        return getDiaryEntry(userId, assignedDay);
    }

    private DiaryFood getActiveDiaryFood(String userId, LocalDate assignedDay,
                                         DiaryFoodEntrySubmission diaryFoodEntrySubmission) {
        Optional<CatalogueUserFood> catalogueUserFood =
                catalogueUserFoodRepository.findByUserIdAndNameAndActive(userId, diaryFoodEntrySubmission.getName(),
                        true);
        if (catalogueUserFood.isEmpty()) {
            return null;
        }
        return new DiaryFood(userId, assignedDay, diaryFoodEntrySubmission.getDateTime(),
                catalogueUserFood.get(), diaryFoodEntrySubmission.getGram());
    }

    public Optional<DiaryEntry> saveDiaryMeal(String userId, LocalDate assignedDay,
                                              DiaryMealEntrySubmission diaryMealEntrySubmission) {
        log.info("Trying to save diary meal entry with assignedDay {} and user with id {}", assignedDay, userId);
        if (diaryMealExists(userId, assignedDay, diaryMealEntrySubmission.getDateTime(),
                diaryMealEntrySubmission.getName())) {
            String message = String.format("DiaryMeal entry with of user with id {}, with assignedDay {}, " +
                            "with dateTime {} and with name {} already exists", userId, assignedDay,
                    diaryMealEntrySubmission.getDateTime(), diaryMealEntrySubmission.getName());
            throw new DatabaseEntryAlreadyExistingException(message);
        }
        DiaryMeal diaryMeal = getDiaryMealByActiveCatalogueMeal(userId, assignedDay, diaryMealEntrySubmission);
        if (diaryMeal == null) {
            return Optional.empty();
        }
        diaryMealRepository.save(diaryMeal);
        return getDiaryEntry(userId, assignedDay);
    }

    private boolean diaryMealExists(String userId, LocalDate assignedDay, LocalDateTime dateTime, String mealName) {
        return diaryMealRepository
                .findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDayAndDiaryMetaDataDateTimeAndCatalogueMealName(
                        userId, assignedDay, dateTime, mealName)
                .isPresent();
    }

    private DiaryMeal getDiaryMealByActiveCatalogueMeal(String userId, LocalDate assignedDay,
                                                        DiaryMealEntrySubmission diaryMealEntrySubmission) {
        Optional<CatalogueMeal> catalogueMeal =
                catalogueMealRepository.findByUserIdAndNameAndActive(userId, diaryMealEntrySubmission.getName(),
                        true);
        if (catalogueMeal.isEmpty()) {
            return null;
        }
        return new DiaryMeal(userId, assignedDay, diaryMealEntrySubmission.getDateTime(), catalogueMeal.get(),
                diaryMealEntrySubmission.getPortion());
    }
}
