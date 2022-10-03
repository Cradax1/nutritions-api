package com.mdaul.nutrition.nutritionapi.exception;

public class DatabaseEntryAlreadyExistingException extends RuntimeException {

    public DatabaseEntryAlreadyExistingException(String message) {
        super(message);
    }
}
