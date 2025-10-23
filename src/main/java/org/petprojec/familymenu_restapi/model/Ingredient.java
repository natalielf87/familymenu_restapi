package org.petprojec.familymenu_restapi.model;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="ingredients")
@Data
@ToString
public class Ingredient {
@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(length=100)
    private String name;

    @Column(length=500)
    private String description;

    @Column(name="created_at")
    private ZonedDateTime createdAt;
    
    @Column(name="updated_at")
    private ZonedDateTime updatedAt;

    @Column(name="is_actual")
    private boolean isActual;

}
