package org.petprojec.familymenu_restapi.repositories;

import org.petprojec.familymenu_restapi.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredient, Long>{

}
