package com.mdaul.nutrition.nutritionapi.model.database;

import com.mdaul.nutrition.nutritionapi.model.database.embedded.CatalogueMealIngredientId;
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
@Table(name = "catalogue_meal_ingredient")
@IdClass(CatalogueMealIngredientId.class)
public class CatalogueMealIngredient {

    @Id
    @ManyToOne
    @JoinColumn(name = "catalogue_meal_id", referencedColumnName = "id")
    private CatalogueMeal catalogueMeal;

    @Id
    @ManyToOne
    @JoinColumn(name = "catalogue_user_food_id", referencedColumnName = "id")
    private CatalogueUserFood catalogueUserFood;

    @Column
    private int gram;

    public CatalogueMealIngredient(int gram, CatalogueMeal catalogueMeal, CatalogueUserFood catalogueUserFood) {
        this.gram = gram;
        this.catalogueMeal = catalogueMeal;
        this.catalogueUserFood = catalogueUserFood;
    }

    public CatalogueMealIngredient(int gram, CatalogueUserFood catalogueUserFood) {
        this.gram = gram;
        this.catalogueUserFood = catalogueUserFood;
    }
}
