package com.mdaul.nutrition.nutritionapi.repository;

import com.mdaul.nutrition.nutritionapi.model.database.DiaryFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryFoodRepository extends JpaRepository<DiaryFood, Long> {

    List<DiaryFood> findAllByDiaryMetaDataUserIdOrderByDiaryMetaDataAssignedDay(String userId);

    List<DiaryFood> findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDay(String userId, LocalDate assignedDay);

    Optional<DiaryFood> findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDayAndCatalogueUserFoodName(
            String userId, LocalDate assignedDay, String name);

    Optional<DiaryFood> findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDayAndDiaryMetaDataDateTimeAndCatalogueUserFoodName(
            String userId, LocalDate assignedDay, LocalDateTime dateTime, String name);

    List<DiaryFood> findByCatalogueUserFoodId(long id);

    @Modifying(clearAutomatically = true)
    @Query("update DiaryFood u set u.gram = ?1 where u.id = ?2")
    int setGramById(int gram, long id);
}
