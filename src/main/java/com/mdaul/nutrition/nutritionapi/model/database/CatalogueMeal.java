package com.mdaul.nutrition.nutritionapi.model.database;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "catalogue_meal")
public class CatalogueMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column
    private boolean active;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "catalogueMeal", cascade = CascadeType.ALL)
    private List<CatalogueMealIngredient> catalogueMealIngredients;

    public CatalogueMeal(String userId, String name, boolean active) {
        this.userId = userId;
        this.name = name;
        this.active = active;
    }
}
