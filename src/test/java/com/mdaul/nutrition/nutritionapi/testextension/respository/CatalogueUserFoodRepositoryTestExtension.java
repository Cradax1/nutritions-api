package com.mdaul.nutrition.nutritionapi.testextension.respository;

import com.mdaul.nutrition.nutritionapi.repository.CatalogueUserFoodRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CatalogueUserFoodRepositoryTestExtension extends CatalogueUserFoodRepository {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete CatalogueUserFood")
    int deleteAllRows();
}
