package org.petprojec.familymenu_restapi.repositories;

import org.petprojec.familymenu_restapi.model.DishesTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishesTrackingRepository extends JpaRepository<DishesTracking, Long>{

}
