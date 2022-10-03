package com.mdaul.nutrition.nutritionapi.controller;

import com.mdaul.nutrition.nutritionapi.api.FoodApi;
import com.mdaul.nutrition.nutritionapi.api.model.Food;
import com.mdaul.nutrition.nutritionapi.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodApiController implements FoodApi {

    private final FoodService foodService;

    public FoodApiController(FoodService foodService) {
        this.foodService = foodService;
    }

    @Override
    public ResponseEntity<Food> foodBarcodeGet(Long barcode) {
        return ResponseEntity.of(foodService.getFood(barcode));
    }
}
