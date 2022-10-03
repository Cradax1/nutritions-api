package com.mdaul.nutrition.nutritionapi.service;

import com.mdaul.nutrition.nutritionapi.api.model.ExternalFoodSubmission;
import com.mdaul.nutrition.nutritionapi.api.model.Food;
import com.mdaul.nutrition.nutritionapi.client.FoodInformationProviderClient;
import com.mdaul.nutrition.nutritionapi.exception.DatabaseEntryAlreadyExistingException;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueExternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueInternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.InformationProviderResult;
import com.mdaul.nutrition.nutritionapi.repository.*;
import com.mdaul.nutrition.nutritionapi.util.DataEntriesOptimizer;
import com.mdaul.nutrition.nutritionapi.util.DataIntegrityValidator;
import com.mdaul.nutrition.nutritionapi.util.builder.CatalogueExternalFoodBuilder;
import com.mdaul.nutrition.nutritionapi.util.builder.CatalogueInternalFoodBuilder;
import com.mdaul.nutrition.nutritionapi.util.builder.FoodBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CatalogueFoodService {
    private final FoodInformationProviderClient providerClient;
    private final CatalogueExternalFoodBuilder externalFoodBuilder;
    private final CatalogueInternalFoodBuilder internalFoodBuilder;
    private final FoodBuilder foodBuilder;
    private final DataIntegrityValidator dataIntegrityValidator;
    private final DataEntriesOptimizer dataEntriesOptimizer;
    @Autowired
    private CatalogueExternalFoodRepository catalogueExternalFoodRepository;
    @Autowired
    private CatalogueInternalFoodRepository catalogueInternalFoodRepository;
    @Autowired
    private CatalogueUserFoodRepository catalogueUserFoodRepository;
    @Autowired
    private CatalogueMealIngredientRepository catalogueMealIngredientRepository;
    @Autowired
    private DiaryFoodRepository diaryFoodRepository;

    public CatalogueFoodService(FoodInformationProviderClient providerClient, FoodBuilder foodBuilder,
                                CatalogueExternalFoodBuilder externalFoodBuilder, CatalogueInternalFoodBuilder internalFoodBuilder,
                                DataIntegrityValidator dataIntegrityValidator, DataEntriesOptimizer dataEntriesOptimizer) {
        this.providerClient = providerClient;
        this.foodBuilder = foodBuilder;
        this.externalFoodBuilder = externalFoodBuilder;
        this.internalFoodBuilder = internalFoodBuilder;
        this.dataIntegrityValidator = dataIntegrityValidator;
        this.dataEntriesOptimizer = dataEntriesOptimizer;
    }

    @Transactional
    public Optional<CatalogueUserFood> deleteFood(String name, String userId) {
        log.info("Deleting food with name {} and userId {}", name, userId);
        Optional<CatalogueUserFood> catalogueUserFood =
                catalogueUserFoodRepository.findByUserIdAndNameAndActive(userId, name, true);
        if (catalogueUserFood.isEmpty()) {
            log.debug("Could not find food with name {} and userId {}", name, userId);
            return Optional.empty();
        }
        log.debug("Setting food with name {} and userId {} to inactive", name, userId);
        catalogueUserFoodRepository.setActiveById(false, catalogueUserFood.get().getId());
        dataEntriesOptimizer.optimizeByCatalogueUserFoodId(catalogueUserFood.get().getId());
        return catalogueUserFood;
    }

    @Transactional
    public Optional<Food> updateFood(Food food, String userId) {
        log.info("Updating food with name {} and userId {}", food.getName(), userId);
        Optional<CatalogueUserFood> catalogueUserFood =
                catalogueUserFoodRepository.findByUserIdAndNameAndActive(userId, food.getName(), true);
        if (catalogueUserFood.isEmpty()) {
            log.info("Could not find food with name {} and userId {}", food.getName(), userId);
            return Optional.empty();
        }
        dataIntegrityValidator.validateContainsExactlyOneFoodType(catalogueUserFood.get());
        setCatalogueUserFoodInactive(catalogueUserFood.get());
        dataEntriesOptimizer.optimizeByCatalogueUserFoodId(catalogueUserFood.get().getId());
        return Optional.of(saveInternalUserFoodDo(food, userId));
    }

    private void setCatalogueUserFoodInactive(CatalogueUserFood catalogueUserFood) {
        catalogueUserFoodRepository.setActiveById(false, catalogueUserFood.getId());
    }

    public Optional<Food> saveExternalUserFood(ExternalFoodSubmission externalFoodSubmission, String userId) {
        log.info("Saving external food with barcode {}, name {} and userId {}",
                externalFoodSubmission.getBarcode(), externalFoodSubmission.getName(), userId);
        if (catalogueUserFoodExists(userId, externalFoodSubmission.getName())) {
            String message = String.format(
                    "Database entry for name %s and userId %s already exists",
                    externalFoodSubmission.getName(), userId);
            throw new DatabaseEntryAlreadyExistingException(message);
        }
        if (catalogueExternalFoodAlreadyMappedToUser(externalFoodSubmission.getBarcode(), userId)) {
            String message = String.format(
                    "Database entry for barcode %d and userId %s already exists",
                    externalFoodSubmission.getBarcode(), userId);
            throw new DatabaseEntryAlreadyExistingException(message);
        }
        Optional<InformationProviderResult> providerResult =
                providerClient.getFoodInformation(externalFoodSubmission.getBarcode());
        if (providerResult.isEmpty()) {
            log.info("Food with barcode {} not found by information provider", externalFoodSubmission.getBarcode());
            return Optional.empty();
        }
        CatalogueUserFood catalogueUserFood = catalogueUserFoodRepository.save(
                new CatalogueUserFood(userId, externalFoodSubmission.getName(), true,
                        getCatalogueExternalFood(providerResult.get())));
        return Optional.of(foodBuilder.build(catalogueUserFood));
    }

    private boolean catalogueExternalFoodAlreadyMappedToUser(long barcode, String userId) {
        return catalogueUserFoodRepository
                .findByCatalogueExternalFoodBarcodeAndUserIdAndActive(barcode, userId, true).isPresent();
    }

    private CatalogueExternalFood getCatalogueExternalFood(InformationProviderResult providerResult) {
        Optional<CatalogueExternalFood> existingCatalogueExternalFood =
                catalogueExternalFoodRepository.findFirstByBarcodeOrderByVersionDesc(providerResult.getCode());
        if (existingCatalogueExternalFood.isEmpty()) {
            log.debug("CatalogueExternalFood of InformationProviderResult not found, building first version");
            return externalFoodBuilder.build(providerResult, 1);
        }
        CatalogueExternalFood potentialNextExternalFood =
                externalFoodBuilder.build(providerResult, existingCatalogueExternalFood.get().getVersion() + 1);
        if (potentialNextExternalFood.getCatalogueFood().equals(
                existingCatalogueExternalFood.get().getCatalogueFood())) {
            log.debug("CatalogueFood of CatalogueExternalFood equals CatalogueFood of InformationProviderResult, " +
                    "do not build new version");
            return existingCatalogueExternalFood.get();
        }
        log.debug("CatalogueFood of CatalogueExternalFood does not equal CatalogueFood of " +
                "InformationProviderResult, building version {}", potentialNextExternalFood.getVersion());
        return potentialNextExternalFood;
    }

    public Optional<Food> getActiveExternalFood(long barcode, String userId) {
        log.info("Trying to find User-Food entry with userId {} and barcode: {}", userId, barcode);
        Optional<CatalogueUserFood> catalogueUserFood = catalogueUserFoodRepository
                .findByCatalogueExternalFoodBarcodeAndUserIdAndActive(barcode, userId, true);
        if (catalogueUserFood.isEmpty()) {
            log.info("User-Food entry with userId {} and barcode: {} not found", userId, barcode);
            return Optional.empty();
        }
        return Optional.of(foodBuilder.build(catalogueUserFood.get()));
    }

    public Food saveInternalUserFood(Food food, String userId) {
        log.info("Saving user defined food with name {} and userId {}", food.getName(), userId);
        if (catalogueUserFoodExists(userId, food.getName())) {
            String message = String.format(
                    "Database entry for name %s and userId %s already exists", food.getName(), userId);
            throw new DatabaseEntryAlreadyExistingException(message);
        }
        return saveInternalUserFoodDo(food, userId);
    }

    private Food saveInternalUserFoodDo(Food food, String userId) {
        CatalogueUserFood catalogueUserFood =
                catalogueUserFoodRepository.save(new CatalogueUserFood(userId, food.getName(), true));
        CatalogueInternalFood catalogueInternalFood = catalogueInternalFoodRepository.save(
                        internalFoodBuilder.build(food.getBrands(), food.getNutriments(), catalogueUserFood));
        return foodBuilder.build(catalogueInternalFood);
    }

    private boolean catalogueUserFoodExists(String userId, String name) {
        return catalogueUserFoodRepository.findByUserIdAndNameAndActive(userId, name, true).isPresent();
    }

    public Optional<List<Food>> getFood(String userId) {
        log.info("Searching for food list of user with id {}", userId);
        List<CatalogueUserFood> catalogueUserFood =
                catalogueUserFoodRepository.findByUserIdAndActive(userId, true);
        return Optional.of(catalogueUserFood.stream().map(foodBuilder::build).toList());
    }

    public Optional<Food> getFood(String name, String userId) {
        log.info("Searching for food by name {} and userId {}", name, userId);
        Optional<CatalogueUserFood> catalogueUserFood =
                catalogueUserFoodRepository.findByUserIdAndNameAndActive(userId, name, true);
        if (catalogueUserFood.isEmpty()) {
            log.info("Could not find food with name {} and userId {}", name, userId);
            return Optional.empty();
        }
        return Optional.of(foodBuilder.build(catalogueUserFood.get()));
    }
}
