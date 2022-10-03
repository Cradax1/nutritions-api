package com.mdaul.nutrition.nutritionapi.util;

import com.mdaul.nutrition.nutritionapi.exception.DataIntegrityException;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueExternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueInternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DataIntegrityValidatorTest {

    private final DataIntegrityValidator dataIntegrityValidator = new DataIntegrityValidator();

    @Test
    void validateContainsExactlyOneFoodType_valid_containsOnlyInternalFood() {
        CatalogueUserFood catalogueUserFood = new CatalogueUserFood();
        catalogueUserFood.setCatalogueInternalFood(new CatalogueInternalFood());

        dataIntegrityValidator.validateContainsExactlyOneFoodType(catalogueUserFood);
    }

    @Test
    void validateContainsExactlyOneFoodType_valid_containsOnlyExternalFood() {
        CatalogueUserFood catalogueUserFood = new CatalogueUserFood();
        catalogueUserFood.setCatalogueExternalFood(new CatalogueExternalFood());

        dataIntegrityValidator.validateContainsExactlyOneFoodType(catalogueUserFood);
    }

    @Test
    void validateContainsExactlyOneFoodType_throwsException_containsInternalAndExternalFood() {
        CatalogueUserFood catalogueUserFood = new CatalogueUserFood();
        catalogueUserFood.setCatalogueInternalFood(new CatalogueInternalFood());
        catalogueUserFood.setCatalogueExternalFood(new CatalogueExternalFood());

        assertThatThrownBy(() -> dataIntegrityValidator.validateContainsExactlyOneFoodType(catalogueUserFood))
                .isInstanceOf(DataIntegrityException.class);
    }

    @Test
    void validateContainsExactlyOneFoodType_throwsException_containsNoFood() {
        CatalogueUserFood catalogueUserFood = new CatalogueUserFood();

        assertThatThrownBy(() -> dataIntegrityValidator.validateContainsExactlyOneFoodType(catalogueUserFood))
                .isInstanceOf(DataIntegrityException.class);
    }
}
