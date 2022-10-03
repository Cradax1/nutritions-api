package com.mdaul.nutrition.nutritionapi.util;

import com.mdaul.nutrition.nutritionapi.exception.DataIntegrityException;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;

public class DataIntegrityValidator {

    public void validateContainsExactlyOneFoodType(CatalogueUserFood catalogueUserFood) {
        if (catalogueUserFood.getCatalogueInternalFood()!=null && catalogueUserFood.getCatalogueExternalFood()!=null) {
            throw new DataIntegrityException("Matching for CatalogueInternalFood and CatalogueExternalFood" +
                " in CatalogueUserFood has been found. There has to be exactly one.");
        }
        if (catalogueUserFood.getCatalogueInternalFood()==null && catalogueUserFood.getCatalogueExternalFood()==null) {
            throw new DataIntegrityException("No matching for CatalogueInternalFood or " +
                "CatalogueExternalFood in CatalogueUserFood has been found. There has to be exactly one.");
        }
    }
}
