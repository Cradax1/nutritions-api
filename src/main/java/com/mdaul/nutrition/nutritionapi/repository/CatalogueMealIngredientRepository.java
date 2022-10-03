package com.mdaul.nutrition.nutritionapi.repository;

import com.mdaul.nutrition.nutritionapi.model.database.CatalogueMealIngredient;
import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueMealIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogueMealIngredientRepository extends JpaRepository<CatalogueMealIngredient, CatalogueMealIngredientId> {

    List<CatalogueMealIngredient> findByCatalogueUserFoodId(long id);
}
