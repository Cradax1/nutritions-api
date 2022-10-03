package com.mdaul.nutrition.nutritionapi.testextension.respository;

import com.mdaul.nutrition.nutritionapi.repository.DiaryFoodRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DiaryFoodRepositoryTestExtension extends DiaryFoodRepository {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete DiaryFood")
    int deleteAllRows();
}
