package org.petprojec.familymenu_restapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.petprojec.familymenu_restapi.dto.DishDTO;
import org.petprojec.familymenu_restapi.model.Dish;
import org.petprojec.familymenu_restapi.repositories.DishesRepository;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class DishesServiceTest {

    private DishesRepository dishesRepository = mock(DishesRepository.class);

    @InjectMocks
    private DishesService dishesService;

    static final long ID1 = 1L;
    static final long ID2 = 2L;

    static final String DISH_NAME1 = "Meatballs";

    private Dish testDish1;
    private Dish testDish2;

    private DishDTO testDishDTO1;

    @BeforeEach
    private void setup(){
        testDish1 = new Dish(DISH_NAME1, 2, "Easy to cook and desirable by the child");
        testDish1.setId(ID1);

        testDishDTO1 = testDish1.getDishDTO();

        testDish2 = new Dish("Tomato soup", 1, "Italian cuisine");
        testDish2.setId(ID2);

    }

    @Test
    public void whenFindAll_thenAllDishesReturned() {
        when(dishesRepository.findAll()).thenReturn(List.of(testDish1, testDish2));

        List<Dish> returnedDishes = dishesService.findAll();

        assertThat(returnedDishes).hasSize(2);
        assertThat(returnedDishes).contains(testDish1);
        assertThat(returnedDishes).contains(testDish2);
    }

    @Test
    public void whenFindAll_thenNoDishesFound() {
        when(dishesRepository.findAll()).thenReturn(Collections.emptyList());

        List<Dish> returnedDishes = dishesService.findAll();

        assertThat(returnedDishes).hasSize(0);
    }

    @Test
    public void whenFindById_thenDishReturned() {
        when(dishesRepository.findById(ID1)).thenReturn(Optional.of(testDish1));

        Optional<Dish> returnedDish = dishesService.findById(ID1);

        assertThat(returnedDish).isPresent();
        assertThat(returnedDish).contains(testDish1);
    }

    @Test
    public void whenFindById_thenDishNotFound() {
        when(dishesRepository.findById(ID1)).thenReturn(Optional.empty());

        Optional<Dish> returnedDish = dishesService.findById(ID1);

        assertThat(returnedDish).isEmpty();
    }

    @Test
    public void whenFindByNameStartingWith_thenDishReturned() {
        final String filterStr = "Meat";
        when(dishesRepository.findByNameStartingWith(filterStr)).thenReturn(List.of(testDish1));

        List<Dish> returnedDishes = dishesService.findByNameStartingWith(filterStr);

        assertThat(returnedDishes).hasSize(1);
        assertThat(returnedDishes).contains(testDish1);
    }

    @Test
    public void whenFindByNameStartingWith_thenNoDishesFound() {
        final String filterStr = "Meat";
        when(dishesRepository.findByNameStartingWith(filterStr)).thenReturn(Collections.emptyList());

        List<Dish> returnedDishes = dishesService.findByNameStartingWith(filterStr);

        assertThat(returnedDishes).hasSize(0);
    }

    @Test
    public void whenSaveDish_thenDishIsPersistedAndIdGenerated(){
        when(dishesRepository.saveAndFlush(any(Dish.class))).thenReturn(testDish1);

        long returnedId = dishesService.save(testDishDTO1);

        assertThat(returnedId==testDish1.getId()).isTrue();
    }

    @Test
    public void whenSaveDish_thenDataIntegrityViolationExceptionThrown(){
        DataIntegrityViolationException dbException = new DataIntegrityViolationException("Duplicate key value violates unique constraint");

        when(dishesRepository.saveAndFlush(any(Dish.class))).thenThrow(dbException);

        DataIntegrityViolationException thrownException = assertThrows(DataIntegrityViolationException.class, 
                                                                () -> dishesService.save(testDishDTO1));

        assertThat(thrownException.getMessage()).isEqualTo(String.format("The dish with the name=\"%s\" already exists", testDishDTO1.getName()));
        
    }

    @Test
    public void whenUpdateDish_thenChangesPersisted() {
        final String newName = testDish1.getName() + " - changed";
        final int newType = 0;
        final String newDescription = testDish1.getDescription() + " - changed";
        final boolean newActual = false;

        when(dishesRepository.findById(ID1)).thenReturn(Optional.of(testDish1));

        Dish expectedDish = new Dish(newName, newType, newDescription);
        expectedDish.setId(ID1);

        when(dishesRepository.saveAndFlush(any(Dish.class))).thenReturn(expectedDish);

        Dish updatedDish = dishesService.update(ID1, newName, newType, newDescription, newActual);

        assertThat(updatedDish).isEqualTo(expectedDish);
    }

    @Test
    public void whenUpdateDish_thenNoExistedDishFound() {

        when(dishesRepository.findById(ID1)).thenReturn(Optional.empty());

        EntityNotFoundException thrownException = assertThrows(EntityNotFoundException.class, 
                () -> dishesService.update(ID1, "anyString", 0, "anyString", false));

        assertThat(thrownException.getMessage()).isEqualTo(String.format("Dish item with id=%d does not exist", ID1));
    }

    @Test
    public void whenUpdateDish_thenDataIntegrityViolationExceptionThrown() {
        final String newName = testDish1.getName() + " - changed";
        final int newType = 0;
        final String newDescription = testDish1.getDescription() + " - changed";
        final boolean newActual = false;

        when(dishesRepository.findById(ID1)).thenReturn(Optional.of(testDish1));

        DataIntegrityViolationException dbException = new DataIntegrityViolationException("Duplicate key value violates unique constraint");

        when(dishesRepository.saveAndFlush(any(Dish.class))).thenThrow(dbException);

        DataIntegrityViolationException thrownException = assertThrows(DataIntegrityViolationException.class, 
                () -> dishesService.update(ID1, newName, newType, newDescription, newActual));

        assertThat(thrownException.getMessage()).isEqualTo(String.format("The dish with the name=\"%s\" already exists", newName));
    }

    @Test
    public void whenPatchDishAllFields_thenChangesPersisted() {
        final Optional<String> newName = Optional.of(testDish1.getName() + " - changed");
        final Optional<Integer> newType = Optional.of(0);
        final Optional<String> newDescription = Optional.of(testDish1.getDescription() + " - changed");
        final Optional<Boolean> newActual = Optional.of(false);

        when(dishesRepository.findById(ID1)).thenReturn(Optional.of(testDish1));

        Dish expectedDish = new Dish(newName.get(), newType.get(), newDescription.get());
        expectedDish.setId(ID1);

        when(dishesRepository.saveAndFlush(any(Dish.class))).thenReturn(expectedDish);

        Dish updatedDish = dishesService.patch(ID1, newName, newType, newDescription, newActual);

        assertThat(updatedDish).isEqualTo(expectedDish);
    }

    @Test
    public void whenPatchDish_thenNoExistedDishFound() {

        when(dishesRepository.findById(ID1)).thenReturn(Optional.empty());

        EntityNotFoundException thrownException = assertThrows(EntityNotFoundException.class, 
                () -> dishesService.patch(ID1, Optional.of("anyString"), Optional.of(0), Optional.of("anyString"), Optional.of(false)));

        assertThat(thrownException.getMessage()).isEqualTo(String.format("Dish item with id=%d does not exist", ID1));
    }

    @Test
    public void whenPatchDish_thenDataIntegrityViolationExceptionThrown() {
        final Optional<String> newName = Optional.of(testDish1.getName() + " - changed");
        final Optional<Integer> newType = Optional.of(0);
        final Optional<String> newDescription = Optional.of(testDish1.getDescription() + " - changed");
        final Optional<Boolean> newActual = Optional.of(false);

        when(dishesRepository.findById(ID1)).thenReturn(Optional.of(testDish1));

        DataIntegrityViolationException dbException = new DataIntegrityViolationException("Duplicate key value violates unique constraint");

        when(dishesRepository.saveAndFlush(any(Dish.class))).thenThrow(dbException);

        DataIntegrityViolationException thrownException = assertThrows(DataIntegrityViolationException.class, 
                () -> dishesService.patch(ID1, newName, newType, newDescription, newActual));

        assertThat(thrownException.getMessage()).isEqualTo(String.format("The dish with the name=\"%s\" already exists", newName.get()));
    }


}
