package org.petprojec.familymenu_restapi.dto.patch;

import java.time.LocalDate;
import java.util.Optional;

import org.petprojec.familymenu_restapi.annotations.OptionalStringSize;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@ToString
public class DishesTrackingPatchDTO {
    @OptionalStringSize(max=500)
    private Optional<String> notes = Optional.empty();
    
    private Optional<LocalDate> dateTo = Optional.empty();

    private Optional<Boolean> isActual = Optional.empty();
}
