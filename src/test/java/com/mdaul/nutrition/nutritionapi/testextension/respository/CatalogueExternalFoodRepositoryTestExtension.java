package com.mdaul.nutrition.nutritionapi.testextension.respository;

import com.mdaul.nutrition.nutritionapi.repository.CatalogueExternalFoodRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CatalogueExternalFoodRepositoryTestExtension extends CatalogueExternalFoodRepository {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete CatalogueExternalFood")
    int deleteAllRows();
}
