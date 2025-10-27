package org.petprojec.familymenu_restapi.repositories;

import java.util.List;
import java.util.Optional;

import org.petprojec.familymenu_restapi.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface DishesRepository extends JpaRepository<Dish, Long>{
    public List<Dish> findByNameStartingWith(String name);

    public Optional<Dish> findByName(String name);

    @Modifying
    public void deleteByName(String Name);
}
