package org.petprojec.familymenu_restapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class DishDTO {
    
    @NotNull
    @Size(max=100)
    @NotBlank
    private String name;

    @NotNull
    @Min(0)
    @Max(3)
    private Integer type;

    @Size(max=500)
    private String description;

    @NotNull
    private Boolean isActual;

}