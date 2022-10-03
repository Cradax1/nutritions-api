package com.mdaul.nutrition.nutritionapi.exception;

import java.io.IOException;

public class FoodInformationProviderNotReachableException extends RuntimeException {

    public FoodInformationProviderNotReachableException(IOException ex) {
        super(ex);
    }
}
