package com.mdaul.nutrition.nutritionapi.model.database.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CatalogueFood {

    @Column
    private String brands;

    @Embedded
    private CatalogueNutriments nutriments;
}
