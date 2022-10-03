package com.mdaul.nutrition.nutritionapi.repository;

import com.mdaul.nutrition.nutritionapi.model.database.CatalogueInternalFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogueInternalFoodRepository extends JpaRepository<CatalogueInternalFood, Long> {
}
