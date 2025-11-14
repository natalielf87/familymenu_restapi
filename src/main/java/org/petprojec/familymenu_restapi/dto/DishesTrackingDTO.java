package org.petprojec.familymenu_restapi.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DishesTrackingDTO {

    @NotNull
    private Long dishId;

    @Size(max=500)
    private String notes;

    @NotNull
    private LocalDate dateFrom;
    
    private LocalDate dateTo;

    @NotNull
    private Boolean isActual;

}
