package com.mdaul.nutrition.nutritionapi.util;

import com.mdaul.nutrition.nutritionapi.model.database.CatalogueExternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMeal;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMealIngredient;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import com.mdaul.nutrition.nutritionapi.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Slf4j
public class DataEntriesOptimizer {

    private final DataIntegrityValidator dataIntegrityValidator;
    @Autowired
    private CatalogueExternalFoodRepository catalogueExternalFoodRepository;
    @Autowired
    private CatalogueUserFoodRepository catalogueUserFoodRepository;
    @Autowired
    private CatalogueMealIngredientRepository catalogueMealIngredientRepository;
    @Autowired
    private CatalogueMealRepository catalogueMealRepository;
    @Autowired
    private DiaryFoodRepository diaryFoodRepository;
    @Autowired
    private DiaryMealRepository diaryMealRepository;

    public DataEntriesOptimizer(DataIntegrityValidator dataIntegrityValidator) {
        this.dataIntegrityValidator = dataIntegrityValidator;
    }

    public void optimizeByCatalogueMealId(long catalogueMealId) {
        Optional<CatalogueMeal> catalogueMealOptional = catalogueMealRepository.findById(catalogueMealId);
        if (catalogueMealOptional.isEmpty()) {
            log.error("meal with id {} does not exist", catalogueMealId);
            return;
        }
        optimize(catalogueMealOptional.get());
    }

    private void optimize(CatalogueMeal catalogueMeal) {
        if (catalogueMeal.isActive()) {
            log.debug("meal with name {} and userId {} is active and cannot be deleted",
                    catalogueMeal.getName(), catalogueMeal.getUserId());
            return;
        }
        if (catalogueMealIsInUse(catalogueMeal)) {
            log.debug("meal with name {} and userId {} is in use and cannot be deleted",
                    catalogueMeal.getName(), catalogueMeal.getUserId());
            return;
        }
        log.debug("delete meal and its ingredients with name {} and userId {}",
                catalogueMeal.getName(), catalogueMeal.getUserId());
        catalogueMealRepository.delete(catalogueMeal);
        log.debug("trying to optimize food entries for all ingredients for meal with name {} and userId {}"
                , catalogueMeal.getName(), catalogueMeal.getUserId());
        for (CatalogueMealIngredient catalogueMealIngredient: catalogueMeal.getCatalogueMealIngredients()) {
            optimize(catalogueMealIngredient.getCatalogueUserFood());
        }
    }
    private boolean catalogueMealIsInUse(CatalogueMeal catalogueMeal) {
        return !diaryMealRepository.findByCatalogueMealId(catalogueMeal.getId()).isEmpty();
    }

    public void optimizeByCatalogueUserFoodId(long catalogueUserFoodId) {
        Optional<CatalogueUserFood> catalogueUserFoodOptional =
                catalogueUserFoodRepository.findById(catalogueUserFoodId);
        if (catalogueUserFoodOptional.isEmpty()) {
            log.error("food with id {} does not exist", catalogueUserFoodId);
            return;
        }
        optimize(catalogueUserFoodOptional.get());
    }

    private void optimize(CatalogueUserFood catalogueUserFood) {
        dataIntegrityValidator.validateContainsExactlyOneFoodType(catalogueUserFood);
        if (catalogueUserFood.isActive()) {
            log.debug("food with name {} and userId {} is active and cannot be deleted",
                    catalogueUserFood.getName(), catalogueUserFood.getUserId());
            return;
        }
        if (catalogueUserFoodIsInUse(catalogueUserFood)) {
            log.debug("food with name {} and userId {} is in use and cannot be deleted",
                    catalogueUserFood.getName(), catalogueUserFood.getUserId());
            return;
        }
        log.debug("delete user food with name {} and userId {} and corresponding food or matching table " +
                        "if external food", catalogueUserFood.getName(), catalogueUserFood.getUserId());
        catalogueUserFoodRepository.delete(catalogueUserFood);
        if (catalogueUserFood.getCatalogueExternalFood() != null &&
                !catalogueExternalFoodIsInUse(catalogueUserFood.getCatalogueExternalFood())) {
            log.debug("CatalogueExternalFood with barcode {} is not in use, deleting it",
                    catalogueUserFood.getCatalogueExternalFood().getBarcode());
            catalogueExternalFoodRepository.delete(catalogueUserFood.getCatalogueExternalFood());
        }
    }

    private boolean catalogueUserFoodIsInUse(CatalogueUserFood catalogueUserFood) {
        return !catalogueMealIngredientRepository.findByCatalogueUserFoodId(catalogueUserFood.getId()).isEmpty()
                || !diaryFoodRepository.findByCatalogueUserFoodId(catalogueUserFood.getId()).isEmpty();
    }

    private boolean catalogueExternalFoodIsInUse(CatalogueExternalFood catalogueExternalFood) {
        return !catalogueUserFoodRepository.findByCatalogueExternalFoodBarcode(catalogueExternalFood.getBarcode())
                .isEmpty();
    }
}
