package  org.petprojec.familymenu_restapi.services;

import java.util.List;
import java.util.Optional;

import org.petprojec.familymenu_restapi.dto.DishDTO;
import org.petprojec.familymenu_restapi.model.Dish;
import org.petprojec.familymenu_restapi.repositories.DishesRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

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

    @Transactional
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

    @Transactional
    public Dish update(long id, String name, int type, String description, boolean isActual) {
        Optional<Dish> dishOpt = dishesRepository.findById(id);
        if (dishOpt.isPresent()) {
            Dish dish = dishOpt.get();
            dish.setName(name);
            dish.setType(type);
            dish.setDescription(description);
            dish.setActual(isActual);
            return dishesRepository.saveAndFlush(dish);
        } else {
            throw new EntityNotFoundException(String.format("Dish item with id=%d does not exist", id));
        }
    }

    @Transactional
    public Dish patch(long id, Optional<String> name, Optional<Integer> type, Optional<String> description, Optional<Boolean> isActual) {
        Optional<Dish> dishOpt = dishesRepository.findById(id);
        if (dishOpt.isPresent()) {
            Dish dish = dishOpt.get();
            if (name.isPresent()) {
                dish.setName(name.get());
            }
            if (type.isPresent()) {
                dish.setType(type.get());
            }
            if (description.isPresent()) {
                dish.setDescription(description.get());
            }
            if (isActual.isPresent()) {
                dish.setActual(isActual.get());
           }
           return dishesRepository.saveAndFlush(dish);
        } else {
            throw new EntityNotFoundException(String.format("Dish item with id=%d does not exist", id));
        }
    }

    @Transactional
    public void deleteById(long id) {
        dishesRepository.deleteById(id);
    }

    @Transactional
    public void deleteByName(String name) {
        dishesRepository.deleteByName(name);
    }

}