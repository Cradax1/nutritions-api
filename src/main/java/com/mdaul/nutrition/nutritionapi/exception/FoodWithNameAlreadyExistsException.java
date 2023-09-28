package com.mdaul.nutrition.nutritionapi.exception;

public class FoodWithNameAlreadyExistsException extends RuntimeException {

    public FoodWithNameAlreadyExistsException(String message) {
        super(message);
    }
}
