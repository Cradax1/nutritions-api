package com.mdaul.nutrition.nutritionapi.repository;

import com.mdaul.nutrition.nutritionapi.model.database.DiaryMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryMealRepository extends JpaRepository<DiaryMeal, Long> {

    List<DiaryMeal> findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDay(String userId, LocalDate assignedDay);

    Optional<DiaryMeal> findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDayAndCatalogueMealName(
            String userId, LocalDate assignedDay, String name);

    Optional<DiaryMeal> findByDiaryMetaDataUserIdAndDiaryMetaDataAssignedDayAndDiaryMetaDataDateTimeAndCatalogueMealName(
            String userId, LocalDate assignedDay, LocalDateTime dateTime, String name);

    List<DiaryMeal> findByCatalogueMealId(long id);

    @Modifying(clearAutomatically = true)
    @Query("update DiaryMeal u set u.portion = ?1 where u.id = ?2")
    int setPortionById(BigDecimal portion, long id);
}
