package org.petprojec.familymenu_restapi.repositories;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.petprojec.familymenu_restapi.model.DishesTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishesTrackingRepository extends JpaRepository<DishesTracking, Long>{
    public Optional<DishesTracking> findByKeyDishIdAndKeyDateFrom(long dishId, LocalDate dateFrom) ;
    public List<DishesTracking> findByKeyDishId(long dishId);
    public void deleteByKeyDishIdAndKeyDateFrom(long dishId, ZonedDateTime dateFrom);
}
