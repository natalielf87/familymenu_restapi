package  org.petprojec.familymenu_restapi.services;

import java.util.List;
import java.util.Optional;

import org.petprojec.familymenu_restapi.model.Dish;
import org.petprojec.familymenu_restapi.repositories.DishesRepository;
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

    public List<Dish> findByNameStartingWith(String name) {
        return dishesRepository.findByNameStartingWith(name);
    }

    public long save(Dish dish) {
        return ((Dish)dishesRepository.saveAndFlush(dish)).getId();
    }

    public long saveNotActual(String name, int type, String description) {
        return ((Dish)dishesRepository.saveAndFlush(new Dish(name, type, description, false))).getId();
    }

    public long saveActual(String name, int type, String description) {
        return ((Dish)dishesRepository.saveAndFlush(new Dish(name, type, description))).getId();
    }

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

    public void deleteByName(String name) {
        Optional<Dish> dish= dishesRepository.findByName(name);
        if (dish.isPresent()) {
            dishesRepository.delete(dish.get());
            dishesRepository.flush();
        } else {
            throw new EntityNotFoundException(String.format("Dish with name=%s not found", name));
        }
    }
}