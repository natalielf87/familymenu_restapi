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
    private Optional<String> name = Optional.empty();

    @OptionalIntegerConstraint(min=0, max=3)
    private Optional<Integer> type = Optional.empty();

    @OptionalStringSize(max=500)
    private Optional<String> description = Optional.empty();

    private Optional<Boolean> isActual = Optional.empty();

}
