package com.mdaul.nutrition.nutritionapi.model.database;

import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueExternalFoodId;
import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueFood;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "catalogue_external_food")
@IdClass(CatalogueExternalFoodId.class)
public class CatalogueExternalFood {

    @Id
    @Column(name = "barcode")
    private long barcode;

    @Id
    @Column(name = "version")
    private int version;

    @Embedded
    private CatalogueFood catalogueFood = new CatalogueFood();
}
