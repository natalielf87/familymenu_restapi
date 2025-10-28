package org.petprojec.familymenu_restapi.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.petprojec.familymenu_restapi.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class DishesRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private DishesRepository dishesRepository;

    private static final String DISH1_NAME = "Pasta Bolognese";
    private static final int DISH1_TYPE = 2;
    private static final String DISH1_DESCRIPTION = ":-)";
    private static final String DISH2_NAME = "Onion Soup";
    private static final int DISH2_TYPE = 1;

    private static final String DISH_UPDATED_NAME = "New updated name";
    private static final String DISH_UPDATED_DESCRIPTION = "Updated description";

    private Dish createDishInDb(String name, int type, boolean isActual) {
        Dish dish = new Dish(name,type,null,isActual);
        return testEntityManager.persistFlushFind(dish);
    }

    @Test
    void whenSaveDish_thenDishIsPersistedAndIdGenerated() {
        Dish newDish = new Dish(DISH1_NAME, DISH1_TYPE, DISH1_DESCRIPTION);
        Dish createdDish = dishesRepository.save(newDish);

        assertThat(createdDish).isNotNull();
        assertThat(createdDish.getName()).isEqualTo(newDish.getName());
        assertThat(createdDish.getId()).isPositive();
        assertThat(createdDish.getType()).isEqualTo(newDish.getType());
        assertThat(createdDish.getDescription()).isEqualTo(newDish.getDescription());
        assertThat(createdDish.isActual()).isEqualTo(newDish.isActual());
        assertThat(createdDish.getCreatedAt()).isNotNull();
        assertThat(createdDish.getUpdatedAt()).isNull();

    }

    @Test
    void whenFindAll_thenAllDishesReturned() {
        Dish dish1 = createDishInDb(DISH1_NAME, DISH1_TYPE, true);
        Dish dish2 = createDishInDb(DISH2_NAME, DISH2_TYPE, true);

        List<Dish> returnedDishes = dishesRepository.findAll();

        assertThat(returnedDishes).hasSize(2);
        assertThat(returnedDishes).contains(dish1);
        assertThat(returnedDishes).contains(dish2);
    }

    @Test
    void whenFindByNameStartingWith_thenDishesReturned() {
        createDishInDb(DISH1_NAME, DISH1_TYPE, false);
        createDishInDb(DISH2_NAME, DISH2_TYPE, true);

        List<Dish> returnedDishes = dishesRepository.findByNameStartingWith("Pasta");

        assertThat(returnedDishes).hasSize(1);
        assertThat(returnedDishes.get(0).getName()).isEqualTo(DISH1_NAME);
        assertThat(returnedDishes.get(0).getType()).isEqualTo(DISH1_TYPE);
        assertThat(returnedDishes.get(0).isActual()).isEqualTo(false);
    }

    @Test
    void whenFindByName_thenDishReturned() {
        Dish dish1 = createDishInDb(DISH1_NAME, DISH1_TYPE, false);
        createDishInDb(DISH2_NAME, DISH2_TYPE, true);

        Optional<Dish> returnedDish = dishesRepository.findByName(DISH1_NAME);

        assertThat(returnedDish).isPresent();
        assertThat(returnedDish.get()).isEqualTo(dish1);
    }

    @Test
    void whenUpdateDish_thenChangesPersisted() {
        Dish dish = createDishInDb(DISH1_NAME, DISH1_TYPE, true);
        long dishId = dish.getId();
        ZonedDateTime originalCreation = dish.getCreatedAt();

        updateDishInDb(dishId);        
        Optional<Dish> updatedDishOptional = dishesRepository.findById(dishId);

        assertThat(updatedDishOptional).isPresent();

        Dish updatedDish = updatedDishOptional.get();

        assertThat(updatedDish.getName()).isEqualTo(DISH_UPDATED_NAME);
        assertThat(updatedDish.getDescription()).isEqualTo(DISH_UPDATED_DESCRIPTION);
        assertThat(updatedDish.getCreatedAt()).isEqualTo(originalCreation);
        assertThat(updatedDish.getUpdatedAt()).isNotNull();
    }

    @Transactional
    private void updateDishInDb(long id) {
        Dish dishToUpdate = dishesRepository.findById(id).orElseThrow();
        dishToUpdate.setName(DISH_UPDATED_NAME);
        dishToUpdate.setDescription(DISH_UPDATED_DESCRIPTION);

        dishesRepository.saveAndFlush(dishToUpdate);
    }

    @Test
    void whenDeleteByName_thenDishRemoved() {
        Dish dish1 = createDishInDb(DISH1_NAME, DISH1_TYPE, false);
        Dish dish2 = createDishInDb(DISH2_NAME, DISH2_TYPE, true);

        assertThat(dishesRepository.findAll()).hasSize(2);

        dishesRepository.deleteByName(dish1.getName());

        // Detach the entity and save changes to db
        testEntityManager.flush();
        testEntityManager.clear();

        List<Dish> remainedDishes = dishesRepository.findAll();

        assertThat(remainedDishes).hasSize(1);
        assertThat(remainedDishes.get(0).getName()).isEqualTo(dish2.getName());
    }
}
