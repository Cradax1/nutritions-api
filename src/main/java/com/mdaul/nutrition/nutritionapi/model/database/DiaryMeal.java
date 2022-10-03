package com.mdaul.nutrition.nutritionapi.model.database;

import com.mdaul.nutrition.nutritionapi.model.database.embedded.DiaryMetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class DiaryMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Embedded
    private DiaryMetaData diaryMetaData = new DiaryMetaData();

    @OneToOne
    private CatalogueMeal catalogueMeal;

    @Column
    private BigDecimal portion;

    public DiaryMeal(String userId, LocalDate assignedDay, LocalDateTime dateTime, CatalogueMeal catalogueMeal, BigDecimal portion) {
        this.getDiaryMetaData().setUserId(userId);
        this.getDiaryMetaData().setDateTime(dateTime);
        this.getDiaryMetaData().setAssignedDay(assignedDay);
        this.catalogueMeal = catalogueMeal;
        this.portion = portion;
    }
}
