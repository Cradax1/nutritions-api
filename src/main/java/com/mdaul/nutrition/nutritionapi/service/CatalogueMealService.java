package com.mdaul.nutrition.nutritionapi.service;

import com.mdaul.nutrition.nutritionapi.api.model.Meal;
import com.mdaul.nutrition.nutritionapi.api.model.MealSubmission;
import com.mdaul.nutrition.nutritionapi.api.model.MealSubmissionIngredient;
import com.mdaul.nutrition.nutritionapi.exception.DatabaseEntryAlreadyExistingException;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMeal;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMealIngredient;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import com.mdaul.nutrition.nutritionapi.repository.CatalogueMealIngredientRepository;
import com.mdaul.nutrition.nutritionapi.repository.CatalogueMealRepository;
import com.mdaul.nutrition.nutritionapi.repository.CatalogueUserFoodRepository;
import com.mdaul.nutrition.nutritionapi.util.DataEntriesOptimizer;
import com.mdaul.nutrition.nutritionapi.util.builder.MealBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CatalogueMealService {
    private final MealBuilder mealBuilder;
    private final DataEntriesOptimizer dataEntriesOptimizer;
    @Autowired
    private CatalogueUserFoodRepository catalogueUserFoodRepository;
    @Autowired
    private CatalogueMealRepository catalogueMealRepository;
    @Autowired
    private CatalogueMealIngredientRepository catalogueMealIngredientRepository;

    public CatalogueMealService(MealBuilder mealBuilder, DataEntriesOptimizer dataEntriesOptimizer) {
        this.mealBuilder = mealBuilder;
        this.dataEntriesOptimizer = dataEntriesOptimizer;
    }

    @Transactional
    public Optional<CatalogueMeal> deleteMeal(String name, String userId) {
        log.info("Deleting meal with name {} and userId {}", name, userId);
        Optional<CatalogueMeal> catalogueMeal =
                catalogueMealRepository.findByUserIdAndNameAndActive(userId, name, true);
        if (catalogueMeal.isEmpty()) {
            log.debug("Could not find meal with name {} and userId {}", name, userId);
            return Optional.empty();
        }
        log.debug("Setting meal with name {} and userId {} to inactive", name, userId);
        catalogueMealRepository.setActiveById(false, catalogueMeal.get().getId());
        dataEntriesOptimizer.optimizeByCatalogueMealId(catalogueMeal.get().getId());
        return catalogueMeal;
    }

    @Transactional
    public Optional<Meal> updateMeal(MealSubmission mealSubmission, String userId) {
        log.info("Updating meal with name {} to user with id {}", mealSubmission.getName(), userId);
        Optional<CatalogueMeal> catalogueMeal =
                catalogueMealRepository.findByUserIdAndNameAndActive(userId, mealSubmission.getName(), true);
        if (catalogueMeal.isEmpty()) {
            return Optional.empty();
        }
        List<CatalogueMealIngredient> catalogueMealIngredients =
                getActiveCatalogueMealIngredientsWithoutMeal(userId, mealSubmission);
        if (catalogueMealIngredients.size() != mealSubmission.getIngredients().size()) {
            log.debug("Could only find {} out of {} ingredients for the meal to save for user with id {}",
                    catalogueMealIngredients.size(), mealSubmission.getIngredients().size(), userId);
            return Optional.empty();
        }
        setCatalogueMealInactive(catalogueMeal.get());
        return saveMealDo(userId, mealSubmission, catalogueMealIngredients);
    }

    private void setCatalogueMealInactive(CatalogueMeal catalogueMeal) {
        catalogueMealRepository.setActiveById(false, catalogueMeal.getId());
    }

    public Optional<Meal> saveMeal(MealSubmission mealSubmission, String userId) {
        log.info("Saving meal with name {} to user with id {}", mealSubmission.getName(), userId);
        if (activeMealExists(userId, mealSubmission.getName())) {
            String message = String.format("Meal for user with id %s and with name %s already exists", userId,
                    mealSubmission.getName());
            throw new DatabaseEntryAlreadyExistingException(message);
        }
        List<CatalogueMealIngredient> catalogueMealIngredients =
                getActiveCatalogueMealIngredientsWithoutMeal(userId, mealSubmission);
        if (catalogueMealIngredients.size() != mealSubmission.getIngredients().size()) {
            log.debug("Could only find {} out of {} ingredients for the meal to save for user with id {}",
                    catalogueMealIngredients.size(), mealSubmission.getIngredients().size(), userId);
            return Optional.empty();
        }
        return saveMealDo(userId, mealSubmission, catalogueMealIngredients);
    }

    private boolean activeMealExists(String userId, String name) {
        Optional<CatalogueMeal> catalogueMeal =
                catalogueMealRepository.findByUserIdAndNameAndActive(userId, name, true);
        return catalogueMeal.isPresent();
    }

    private Optional<Meal> saveMealDo(String userId, MealSubmission mealSubmission,
                                      List<CatalogueMealIngredient> catalogueMealIngredients) {
        CatalogueMeal catalogueMeal = catalogueMealRepository.save(
                new CatalogueMeal(userId, mealSubmission.getName(), true));
        catalogueMeal.setCatalogueMealIngredients(
                saveCatalogueMealIngredients(catalogueMeal, catalogueMealIngredients));
        return Optional.of(mealBuilder.build(catalogueMeal));
    }

    private List<CatalogueMealIngredient> getActiveCatalogueMealIngredientsWithoutMeal(String userId,
                                                                                       MealSubmission mealSubmission) {
        List<CatalogueMealIngredient> catalogueMealIngredients = new LinkedList<>();
        for (MealSubmissionIngredient ingredientsInner : mealSubmission.getIngredients()) {
            Optional<CatalogueUserFood> catalogueUserFood =
                    catalogueUserFoodRepository.findByUserIdAndNameAndActive(userId, ingredientsInner.getName(), true);
            if (catalogueUserFood.isEmpty()) {
                log.debug("User food with userId {} and name {} not found", userId, ingredientsInner.getName());
            } else {
                catalogueMealIngredients.add(
                        new CatalogueMealIngredient(ingredientsInner.getGram(), catalogueUserFood.get()));
            }
        }
        return catalogueMealIngredients;
    }

    private List<CatalogueMealIngredient> saveCatalogueMealIngredients(CatalogueMeal catalogueMeal,
                                                                       List<CatalogueMealIngredient> catalogueMealIngredients) {
        List<CatalogueMealIngredient> catalogueMealIngredientsResult = new LinkedList<>();
        for (CatalogueMealIngredient catalogueMealIngredient : catalogueMealIngredients) {
            catalogueMealIngredientsResult.add(catalogueMealIngredientRepository.save(
                    new CatalogueMealIngredient(catalogueMealIngredient.getGram(), catalogueMeal,
                            catalogueMealIngredient.getCatalogueUserFood())));
        }
        return catalogueMealIngredientsResult;
    }

    public Optional<List<Meal>> getMeal(String userId) {
        log.info("Trying to find all meals of user with id {}", userId);
        List<CatalogueMeal> catalogueMeal = catalogueMealRepository.findByUserIdAndActive(userId, true);
        return Optional.of(catalogueMeal.stream().map(mealBuilder::build).toList());
    }

    public Optional<Meal> getMeal(String name, String userId) {
        log.info("Trying to find meal with name {} and user with id {}", name, userId);
        Optional<CatalogueMeal> catalogueMeal =
                catalogueMealRepository.findByUserIdAndNameAndActive(userId, name, true);
        if (catalogueMeal.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mealBuilder.build(catalogueMeal.get()));
    }
}
