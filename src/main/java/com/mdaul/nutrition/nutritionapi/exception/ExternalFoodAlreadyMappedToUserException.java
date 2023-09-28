package com.mdaul.nutrition.nutritionapi.exception;

public class ExternalFoodAlreadyMappedToUserException extends RuntimeException {

    public ExternalFoodAlreadyMappedToUserException(String message) {
        super(message);
    }
}
