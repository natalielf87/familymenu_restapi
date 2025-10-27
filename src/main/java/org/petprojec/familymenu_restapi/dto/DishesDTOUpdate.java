package org.petprojec.familymenu_restapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class DishesDTOUpdate {
    private String name;

    @Min(0)
    @Max(3)
    private Integer type;

    @Size(max=500)
    private String description;

    private Boolean isActual;

}
