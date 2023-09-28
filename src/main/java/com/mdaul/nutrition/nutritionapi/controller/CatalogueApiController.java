package com.mdaul.nutrition.nutritionapi.controller;

import com.mdaul.nutrition.nutritionapi.api.CatalogueApi;
import com.mdaul.nutrition.nutritionapi.api.model.ExternalFoodSubmission;
import com.mdaul.nutrition.nutritionapi.api.model.Food;
import com.mdaul.nutrition.nutritionapi.api.model.Meal;
import com.mdaul.nutrition.nutritionapi.api.model.MealSubmission;
import com.mdaul.nutrition.nutritionapi.service.CatalogueFoodService;
import com.mdaul.nutrition.nutritionapi.service.CatalogueMealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CatalogueApiController implements ApiController, CatalogueApi {

    private final CatalogueFoodService catalogueFoodService;
    private final CatalogueMealService catalogueMealService;

    public CatalogueApiController(CatalogueFoodService catalogueFoodService, CatalogueMealService catalogueMealService) {
        this.catalogueFoodService = catalogueFoodService;
        this.catalogueMealService = catalogueMealService;
    }

    @Override
    public ResponseEntity<Food> catalogueFoodExternalPost(ExternalFoodSubmission externalFoodSubmission) {
        Optional<Food> food = catalogueFoodService.saveExternalUserFood(externalFoodSubmission, getUserId());
        return food.map(value
                -> new ResponseEntity<>(value, HttpStatus.CREATED)).orElseGet(()
                -> ResponseEntity.of(food));
    }

    @Override
    public ResponseEntity<Food> catalogueFoodExternalBarcodeGet(Long barcode) {
        return ResponseEntity.of(catalogueFoodService.getActiveExternalFood(barcode, getUserId()));
    }

    @Override
    public ResponseEntity<Food> catalogueFoodPost(Food food) {
        Food foodResult = catalogueFoodService.saveInternalUserFood(food, getUserId());
        return new ResponseEntity<>(foodResult, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<Food>> catalogueFoodGet() {
        return ResponseEntity.of(catalogueFoodService.getFood(getUserId()));
    }

    @Override
    public ResponseEntity<Food> catalogueFoodNameGet(String name) {
        return ResponseEntity.of(catalogueFoodService.getFood(name, getUserId()));
    }

    @Override
    public ResponseEntity<Void> catalogueFoodNameDelete(String name) {
        if (catalogueFoodService.deleteFood(name, getUserId()).isEmpty()) {
            return ResponseEntity.of(Optional.empty());
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Food> catalogueFoodPut(Food food) {
        return ResponseEntity.of(catalogueFoodService.updateFood(food, getUserId()));
    }

    @Override
    public ResponseEntity<List<Food>> catalogueFoodSearchNameGet(String name) {
        return ResponseEntity.of(catalogueFoodService.searchFood(name, getUserId()));
    }

    @Override
    public ResponseEntity<Meal> catalogueMealPost(MealSubmission mealSubmission) {
        Optional<Meal> meal = catalogueMealService.saveMeal(mealSubmission, getUserId());
        return meal.map(value
                -> new ResponseEntity<>(value, HttpStatus.CREATED)).orElseGet(()
                -> ResponseEntity.of(meal));
    }

    @Override
    public ResponseEntity<List<Meal>> catalogueMealGet() {
        return ResponseEntity.of(catalogueMealService.getMeal(getUserId()));
    }

    @Override
    public ResponseEntity<Meal> catalogueMealNameGet(String name) {
        return ResponseEntity.of(catalogueMealService.getMeal(name, getUserId()));
    }

    @Override
    public ResponseEntity<Void> catalogueMealNameDelete(String name) {
        if (catalogueMealService.deleteMeal(name, getUserId()).isEmpty()) {
            return ResponseEntity.of(Optional.empty());
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Meal> catalogueMealPut(MealSubmission mealSubmission) {
        return ResponseEntity.of(catalogueMealService.updateMeal(mealSubmission, getUserId()));
    }
}
