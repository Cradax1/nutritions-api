package com.mdaul.nutrition.nutritionapi.model.database;

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
@Table(name = "catalogue_internal_food")
public class CatalogueInternalFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Embedded
    private CatalogueFood catalogueFood = new CatalogueFood();

    @OneToOne
    @MapsId
    @JoinColumn(name = "catalogue_user_food_id")
    private CatalogueUserFood catalogueUserFood;

    public CatalogueInternalFood(CatalogueFood catalogueFood, CatalogueUserFood catalogueUserFood) {
        this.catalogueFood = catalogueFood;
        this.catalogueUserFood = catalogueUserFood;
    }
}
