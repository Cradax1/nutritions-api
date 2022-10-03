package com.mdaul.nutrition.nutritionapi.model.database.embedded;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatalogueMealIngredientId implements Serializable {

    private long catalogueMeal;
    private long catalogueUserFood;
}
