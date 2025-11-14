package org.petprojec.familymenu_restapi.model;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.petprojec.familymenu_restapi.dto.DishesTrackingDTO;
import org.petprojec.familymenu_restapi.model.keys.DishesTrackingKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="dishestracking")
@Data
@ToString
public class DishesTracking {

    @EmbeddedId
    private DishesTrackingKey key;

    @Column(length=500)
    private String notes;
    
    @Column(name="date_to")
    private LocalDate dateTo;
    
    @Column(name="created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();
    
    @Column(name="updated_at")
    private ZonedDateTime updatedAt;

    @Column(name="is_actual")
    private boolean isActual;

    @ManyToOne
    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    private Dish dish;

    public DishesTracking() {
        this.key = new DishesTrackingKey();
    }

    public DishesTracking(DishesTrackingDTO dishesTrackingDTO) {
        this.key = new DishesTrackingKey(dishesTrackingDTO.getDishId(), dishesTrackingDTO.getDateFrom());
        this.dateTo = dishesTrackingDTO.getDateTo();
        this.isActual = dishesTrackingDTO.getIsActual();
    }

    public DishesTracking(long dishId, String notes, LocalDate dateFrom, LocalDate dateTo, boolean isActual) {
        this.key = new DishesTrackingKey(dishId, dateFrom);
        this.notes = notes;
        this.dateTo = dateTo;
        this.isActual = isActual;
    }

    @PreUpdate
    private void updateTimestamp() {
        this.setUpdatedAt(ZonedDateTime.now());

    }

    public DishesTrackingDTO getDishesTrackingDTO(){
        return new DishesTrackingDTO(this.getKey().getDishId(), this.getNotes(), this.getKey().getDateFrom(), this.getDateTo(), this.isActual());
    }

}
