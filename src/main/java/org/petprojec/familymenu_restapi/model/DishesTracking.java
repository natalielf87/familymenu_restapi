package org.petprojec.familymenu_restapi.model;

import java.time.ZonedDateTime;

import org.petprojec.familymenu_restapi.model.keys.DishesTrackingKey;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="dishestracking")
@IdClass(DishesTrackingKey.class)
@Data
@ToString
public class DishesTracking {
    
    @Id
    @Column(name="dish_id")
    private long dishId;

    @Column(length=500)
    private String notes;

    @Id
    @Column(name="date_from")
    private ZonedDateTime dateFrom;
    
    @Column(name="date_to")
    private ZonedDateTime dateTo;
    
    @Column(name="created_at")
    private ZonedDateTime createdAt;
    
    @Column(name="updated_at")
    private ZonedDateTime updatedAt;

    @Column(name="is_actual")
    private boolean isActual;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

}
