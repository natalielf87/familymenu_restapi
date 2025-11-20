package  org.petprojec.familymenu_restapi.services;

import java.util.List;
import java.util.Optional;

import org.petprojec.familymenu_restapi.dto.DishDTO;
import org.petprojec.familymenu_restapi.model.Dish;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import org.petprojec.familymenu_restapi.repositories.DishesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class DishesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DishesService.class);
    private final DishesRepository dishesRepository;

    public DishesService(DishesRepository dishesRepository) {
        this.dishesRepository = dishesRepository;
    }

    public List<Dish> findAll(){
        LOGGER.debug("findAll() started");
        return dishesRepository.findAll();
    }

    public Optional<Dish> findById(long id) {
        LOGGER.debug(String.format("findById() started with dishId=%d", id));
        Optional<Dish> foundDishOptional = dishesRepository.findById(id);
        LOGGER.info(String.format("findById() dish with id=%d", id) + (foundDishOptional.isPresent() ? " found": " not found"));
        return foundDishOptional;
    }

    public List<Dish> findByNameStartingWith(String name) {
        LOGGER.debug(String.format("findByNameStartingWith() started with name=%s", name));
        List<Dish> dishesList = dishesRepository.findByNameStartingWith(name);
        LOGGER.info(String.format("findByNameStartingWith() dishes") + (dishesList.isEmpty() ? " not found": " found"));
        return dishesList;
    }

    @Transactional
    public long save(DishDTO dish) throws DataIntegrityViolationException {
        LOGGER.debug(String.format("save() started with dishDTO={%s}", dish));
        try {
            Dish addedDish = (Dish)dishesRepository.saveAndFlush(new Dish(dish));
            LOGGER.info(String.format("save() data are added to data storage"));
            return addedDish.getId();
        } catch (DataIntegrityViolationException e) {
            LOGGER.error(String.format("save() data are not saved in data storage: %s", e.getMessage()));
            throw new DataIntegrityViolationException(
                        String.format("The dish with the name=\"%s\" already exists", dish.getName())
            );
        }
    }

    @Transactional
    public Dish update(long id, String name, int type, String description, boolean isActual) {
        LOGGER.debug(String.format("update() started with id=%d, name=%s, type=%d, description=\"%s\", isActual=%b", id, name, type, description, isActual));
        Optional<Dish> dishOpt = dishesRepository.findById(id);
        if (dishOpt.isPresent()) {
            Dish dish = dishOpt.get();
            dish.setName(name);
            dish.setType(type);
            dish.setDescription(description);
            dish.setActual(isActual);
            try {
                Dish savedDish = dishesRepository.saveAndFlush(dish);
                LOGGER.info(String.format("update() data are saved in data storage "));
                return savedDish;
            }
            catch (DataIntegrityViolationException e) {
                LOGGER.error(String.format("update() data are not saved in data storage: %s", e.getMessage()));
               throw new DataIntegrityViolationException(
                        String.format("The dish with the name=\"%s\" already exists", name)
                ); 
            }
        } else {
            LOGGER.error(String.format("update() data are not saved in data storage: Dish item with id=%d not found",id));
            throw new EntityNotFoundException(String.format("Dish item with id=%d does not exist", id));
        }
    }

    @Transactional
    public Dish patch(long id, Optional<String> name, Optional<Integer> type, Optional<String> description, Optional<Boolean> isActual) {
        LOGGER.debug(String.format("patch() started with id=%d, name=%s, type=%s, description=\"%s\", isActual=%s", 
                                        id, name, type, description, isActual));
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
            try {
                Dish savedDish = dishesRepository.saveAndFlush(dish);
                LOGGER.info(String.format("patch() data are saved in data storage"));
                return savedDish;
            }
            catch (DataIntegrityViolationException e) {
                LOGGER.error(String.format("patch() data are not saved in data storage: %s", e.getMessage()));
               throw new DataIntegrityViolationException(
                        String.format("The dish with the name=\"%s\" already exists", name.get())
                ); 
            }
        } else {
            LOGGER.error(String.format("patch() data are not saved in data storage: Dish item with id=%d not found",id));
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