package com.mdaul.nutrition.nutritionapi.repository;

import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogueMealRepository extends JpaRepository<CatalogueMeal, Long> {

    List<CatalogueMeal> findByUserIdAndActive(String userId, boolean active);

    Optional<CatalogueMeal> findByUserIdAndNameAndActive(String userId, String name, boolean active);

    @Modifying(clearAutomatically = true)
    @Query("update CatalogueMeal u set u.active = ?1 where u.id = ?2")
    int setActiveById(boolean active, long id);
}
