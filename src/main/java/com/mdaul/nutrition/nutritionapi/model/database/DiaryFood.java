package com.mdaul.nutrition.nutritionapi.model.database;

import com.mdaul.nutrition.nutritionapi.model.database.embedded.DiaryMetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class DiaryFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Embedded
    private DiaryMetaData diaryMetaData = new DiaryMetaData();

    @OneToOne
    private CatalogueUserFood catalogueUserFood;

    @Column
    private int gram;

    public DiaryFood(String userId, LocalDate assignedDay, LocalDateTime dateTime, CatalogueUserFood catalogueUserFood, int gram) {
        this.getDiaryMetaData().setUserId(userId);
        this.getDiaryMetaData().setDateTime(dateTime);
        this.getDiaryMetaData().setAssignedDay(assignedDay);
        this.catalogueUserFood = catalogueUserFood;
        this.gram = gram;
    }
}
