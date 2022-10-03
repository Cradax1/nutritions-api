package com.mdaul.nutrition.nutritionapi.model.database.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Slf4j
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CatalogueNutriments {

    @Column
    private int calories;

    @Column(nullable = false, scale = 2)
    private BigDecimal carbohydrates;

    @Column(nullable = false, scale = 2)
    private BigDecimal proteins;

    @Column(nullable = false, scale = 2)
    private BigDecimal fat;

    @Column(nullable = false, scale = 2)
    private BigDecimal fiber;

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this)
            return true;

        if (!(obj instanceof CatalogueNutriments catalogueNutriments))
            return false;

        if (catalogueNutriments.calories != this.calories) return false;
        if (catalogueNutriments.carbohydrates.floatValue() != this.carbohydrates.floatValue()) return false;
        if (catalogueNutriments.proteins.floatValue() != this.proteins.floatValue()) return false;
        if (catalogueNutriments.fat.floatValue() != this.fat.floatValue()) return false;
        return catalogueNutriments.fiber.floatValue() == this.fiber.floatValue();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + calories;
        result = 31 * result + (int) (Math.pow(10, 7) * carbohydrates.floatValue());
        result = 31 * result + (int) (Math.pow(10, 7) * proteins.floatValue());
        result = 31 * result + (int) (Math.pow(10, 7) * fat.floatValue());
        result = 31 * result + (int) (Math.pow(10, 7) * fiber.floatValue());
        return result;
    }
}
