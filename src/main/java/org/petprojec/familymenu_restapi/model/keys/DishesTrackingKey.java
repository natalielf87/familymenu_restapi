package org.petprojec.familymenu_restapi.model.keys;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DishesTrackingKey implements Serializable{
    @Column(name="dish_id")
    private long dishId;
    
    @Column(name="date_from")
    private LocalDate dateFrom;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DishesTrackingKey other = (DishesTrackingKey) obj;
        if (dishId != other.dishId)
            return false;
        if (dateFrom == null) {
            if (other.dateFrom != null)
                return false;
        } else if (!dateFrom.equals(other.dateFrom))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (dishId ^ (dishId >>> 32));
        result = prime * result + ((dateFrom == null) ? 0 : dateFrom.hashCode());
        return result;
    }

    
}
