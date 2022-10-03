package com.mdaul.nutrition.nutritionapi.model.database.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DiaryMetaData {

    @Column
    private String userId;

    @Column
    private LocalDate assignedDay;

    @Column
    private LocalDateTime dateTime;
}
