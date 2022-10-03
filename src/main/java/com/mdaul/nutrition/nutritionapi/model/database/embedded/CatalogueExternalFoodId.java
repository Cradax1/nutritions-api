package com.mdaul.nutrition.nutritionapi.model.database.embedded;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatalogueExternalFoodId implements Serializable {

    private long barcode;
    private int version;
}
