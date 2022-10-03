package com.mdaul.nutrition.nutritionapi.repository;

import com.mdaul.nutrition.nutritionapi.model.database.CatalogueExternalFood;
import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueExternalFoodId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatalogueExternalFoodRepository extends JpaRepository<CatalogueExternalFood, CatalogueExternalFoodId> {

    Optional<CatalogueExternalFood> findFirstByBarcodeOrderByVersionDesc(long barcode);
}
