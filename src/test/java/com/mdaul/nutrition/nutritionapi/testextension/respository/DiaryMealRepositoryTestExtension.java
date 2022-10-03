package com.mdaul.nutrition.nutritionapi.testextension.respository;

import com.mdaul.nutrition.nutritionapi.repository.DiaryMealRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DiaryMealRepositoryTestExtension extends DiaryMealRepository {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete DiaryMeal")
    int deleteAllRows();
}
