package com.mdaul.nutrition.nutritionapi.repository;

import com.mdaul.nutrition.nutritionapi.model.database.CatalogueUserFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogueUserFoodRepository extends JpaRepository<CatalogueUserFood, Long> {

    List<CatalogueUserFood> findByUserIdAndActive(String userId, boolean active);
    Optional<CatalogueUserFood> findByUserIdAndNameAndActive(String userId, String name, boolean active);
    List<CatalogueUserFood> findByUserIdAndNameIgnoreCaseContainingAndActive(String userId, String name, boolean active);
    Optional<CatalogueUserFood> findByCatalogueExternalFoodBarcodeAndUserIdAndActive(long barcode, String userId, boolean active);
    List<CatalogueUserFood> findByCatalogueExternalFoodBarcode(long barcode);

    @Modifying(clearAutomatically = true)
    @Query("update CatalogueUserFood u set u.active = ?1 where u.id = ?2")
    int setActiveById(boolean active, long id);
}
