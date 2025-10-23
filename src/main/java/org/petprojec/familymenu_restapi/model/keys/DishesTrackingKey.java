package org.petprojec.familymenu_restapi.model.keys;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DishesTrackingKey implements Serializable{
    private long dishId;
    private ZonedDateTime dateFrom;
}
