package org.petprojec.familymenu_restapi.model;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name="dishes")
@Data
@ToString
public class Dish {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(length=100)
    @NonNull
    private String name;

    private int type;

    @Column(length=500)
    private String description;

    @Column(name="created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();
    
    @Column(name="updated_at")
    private ZonedDateTime updatedAt;

    @Column(name="is_actual")
    private boolean isActual = true;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="dishesingredients", joinColumns = @JoinColumn(name="id"), inverseJoinColumns=@JoinColumn(name="ingredient_id"))
    private List<Ingredient> Ingredients;

    public Dish(String name, int type, String description, boolean isActual) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.isActual = isActual;
    }

    public Dish(String name, int type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    @OneToMany(mappedBy="dish", cascade=CascadeType.ALL)
    private List<DishesTracking> dishesTrackings;


}
