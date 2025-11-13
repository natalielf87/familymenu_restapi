package org.petprojec.familymenu_restapi.dto.patch;

import java.util.Optional;

import org.petprojec.familymenu_restapi.annotations.OptionalIntegerConstraint;
import org.petprojec.familymenu_restapi.annotations.OptionalStringSize;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class DishPatchDTO {
    @OptionalStringSize(max=100)
    private Optional<String> name;

    @OptionalIntegerConstraint(min=0, max=3)
    private Optional<Integer> type;

    @OptionalStringSize(max=500)
    private Optional<String> description;

    private Optional<Boolean> isActual;

}
