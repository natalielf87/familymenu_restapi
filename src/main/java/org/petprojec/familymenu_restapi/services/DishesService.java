package  org.petprojec.familymenu_restapi.services;

import java.util.List;
import java.util.Optional;

import org.petprojec.familymenu_restapi.dto.DishDTO;
import org.petprojec.familymenu_restapi.model.Dish;
import org.petprojec.familymenu_restapi.repositories.DishesRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
//@ComponentScan(basePackages = {"org.petprojec.familymenu_restapi.repositories"})
public class DishesService {
    private final DishesRepository dishesRepository;

    public DishesService(DishesRepository dishesRepository) {
        this.dishesRepository = dishesRepository;
    }

    public List<Dish> findAll(){
        return dishesRepository.findAll();
    }

    public Optional<Dish> findById(long id) {
        return dishesRepository.findById(id);
    }

    public List<Dish> findByNameStartingWith(String name) {
        return dishesRepository.findByNameStartingWith(name);
    }

    public long save(DishDTO dish) throws DataIntegrityViolationException {
        //if (dishesRepository.findByName(dish.getName()).isPresent()) 
        try {
            Dish insertedDish = (Dish)dishesRepository.saveAndFlush(new Dish(dish));
            return insertedDish.getId();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                        String.format("The dish with the name=\"%s\" already exists", dish.getName())
            );
        }
    }

    // public long saveNotActual(String name, int type, String description) {
    //     return ((Dish)dishesRepository.saveAndFlush(new Dish(name, type, description, false))).getId();
    // }

    // public long saveActual(String name, int type, String description) {
    //     return ((Dish)dishesRepository.saveAndFlush(new Dish(name, type, description))).getId();
    // }

    public void update(long id, String name, int type, String description, boolean isActual) throws EntityNotFoundException{
        this.update(new Dish(name, type, description,isActual));
    }

    public void update(Dish dish) {
        if (dishesRepository.existsById(dish.getId())) {
            dishesRepository.saveAndFlush(dish);
        } else {
            throw new EntityNotFoundException(String.format("Dish item with id=%d does not exist", dish.getId()));
        }
    } 

    public void deleteById(long id) {
        dishesRepository.deleteById(id);
        dishesRepository.flush();
    }

}