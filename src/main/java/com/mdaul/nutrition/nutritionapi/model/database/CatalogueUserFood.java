package com.mdaul.nutrition.nutritionapi.model.database;

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
@Table(name = "catalogue_user_food")
public class CatalogueUserFood {

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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinTable(name = "catalogue_matching_user_food_external_food",
            joinColumns = {
                    @JoinColumn(name = "catalogue_user_food_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "catalogue_external_food_barcode", referencedColumnName = "barcode"),
                    @JoinColumn(name = "catalogue_external_food_version", referencedColumnName = "version")})
    private CatalogueExternalFood catalogueExternalFood;

    @OneToOne(mappedBy = "catalogueUserFood", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private CatalogueInternalFood catalogueInternalFood;

    public CatalogueUserFood(String userId, String name, boolean active, CatalogueExternalFood catalogueExternalFood) {
        this.userId = userId;
        this.name = name;
        this.active = active;
        this.catalogueExternalFood = catalogueExternalFood;
    }

    public CatalogueUserFood(String userId, String name, boolean active) {
        this.userId = userId;
        this.name = name;
        this.active = active;
    }
}
