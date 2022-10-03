package com.mdaul.nutrition.nutritionapi;

import com.mdaul.nutrition.nutritionapi.testextension.respository.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class TestRepositoryUtils {

    @Autowired
    private CatalogueExternalFoodRepositoryTestExtension catalogueExternalFoodRepositoryTestExtension;
    @Autowired
    private CatalogueInternalFoodRepositoryTestExtension catalogueInternalFoodRepositoryTestExtension;
    @Autowired
    private CatalogueUserFoodRepositoryTestExtension catalogueUserFoodRepositoryTestExtension;
    @Autowired
    private CatalogueMealRepositoryTestExtension catalogueMealRepositoryTestExtension;
    @Autowired
    private CatalogueMealIngredientRepositoryTestExtension catalogueMealIngredientRepositoryTestExtension;
    @Autowired
    private DiaryFoodRepositoryTestExtension diaryFoodRepositoryTestExtension;
    @Autowired
    private DiaryMealRepositoryTestExtension diaryMealRepositoryTestExtension;

    public void clearAllDatabaseRows() {
        diaryMealRepositoryTestExtension.deleteAllRows();
        diaryFoodRepositoryTestExtension.deleteAllRows();
        catalogueMealIngredientRepositoryTestExtension.deleteAllRows();
        catalogueMealRepositoryTestExtension.deleteAllRows();
        catalogueInternalFoodRepositoryTestExtension.deleteAllRows();
        catalogueUserFoodRepositoryTestExtension.deleteAllRows();
        catalogueExternalFoodRepositoryTestExtension.deleteAllRows();
    }
}
