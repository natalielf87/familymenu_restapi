package org.petprojec.familymenu_restapi.repositories;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.petprojec.familymenu_restapi.model.Dish;
import org.petprojec.familymenu_restapi.model.DishesTracking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import jakarta.transaction.Transactional;

@DataJpaTest
public class DishesTrackingRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private DishesTrackingRepository dishesTrackingRepository;

    private static final String DISH1_NAME = "Pasta Bolognese";
    private static final int DISH1_TYPE = 2;
    private static final boolean DISH1_IS_ACTUAL = true;
//    private static final String DISH1_DESCRIPTION = ":-)";
//    private static final String DISH2_NAME = "Meatballs";
    // private static final int DISH2_TYPE = 1;

    private static final String DISH_TRACK1_NOTES = "Notes for the dish tracking 1";
    private static final LocalDate DISH_TRACK1_DATE_FROM = LocalDate.now();
    private static final LocalDate DISH_TRACK1_DATE_TO = LocalDate.now().plusDays(1);
    private static final boolean DISH_TRACK1_IS_ACTUAL = true;

    private static final LocalDate DISH_TRACK2_DATE_FROM = LocalDate.now().plusDays(2);

    private Dish createDishInDb(String name, int type, boolean isActual) {
        Dish dish = new Dish(name,type,null,isActual);
        return testEntityManager.persistFlushFind(dish);
    }

    private DishesTracking createDishesTrackingInDb(String notes, LocalDate dateFrom, LocalDate dateTo, boolean isActual) {
        Dish newDish = createDishInDb(DISH1_NAME, DISH1_TYPE, DISH1_IS_ACTUAL);
        DishesTracking track = new DishesTracking(newDish.getId(),notes,dateFrom,dateTo,isActual);
        track.setDish(newDish);
        return testEntityManager.persistFlushFind(track);
    }

    @Test
    public void whenFindByDishIdAndDateFrom_thenDishesTrackingReturned() {
        DishesTracking track1 = createDishesTrackingInDb(DISH_TRACK1_NOTES, DISH_TRACK1_DATE_FROM, DISH_TRACK1_DATE_TO, DISH_TRACK1_IS_ACTUAL);

        Optional<DishesTracking> actual = dishesTrackingRepository.findByKeyDishIdAndKeyDateFrom(track1.getKey().getDishId(), track1.getKey().getDateFrom());

        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(track1);
    }

    @Test
    void whenSaveDishesTracking_thenDishesTrackingIsPersisted() {
        Dish newDish = createDishInDb(DISH1_NAME, DISH1_TYPE, DISH1_IS_ACTUAL);
        DishesTracking track = new DishesTracking(newDish.getId(),DISH_TRACK1_NOTES,DISH_TRACK1_DATE_FROM,DISH_TRACK1_DATE_TO,DISH_TRACK1_IS_ACTUAL);
        track.setDish(newDish);
        DishesTracking actualSavedTrack = dishesTrackingRepository.save(track);

        assertThat(actualSavedTrack).isNotNull();
        assertThat(actualSavedTrack.getKey().getDishId()).isEqualTo(track.getKey().getDishId());
        assertThat(actualSavedTrack.getKey().getDateFrom()).isEqualTo(track.getKey().getDateFrom());
        assertThat(actualSavedTrack.getNotes()).isEqualTo(track.getNotes());
        assertThat(actualSavedTrack.isActual()).isEqualTo(track.isActual());
        assertThat(actualSavedTrack.getCreatedAt()).isNotNull();
        assertThat(actualSavedTrack.getUpdatedAt()).isNull();

    }

    @Test
    void whenFindAll_thenAllDishesTrackingReturned() {
        DishesTracking track1 = createDishesTrackingInDb(DISH_TRACK1_NOTES, DISH_TRACK1_DATE_FROM,DISH_TRACK1_DATE_TO, DISH_TRACK1_IS_ACTUAL);
        DishesTracking track2 = createDishesTrackingInDb(DISH_TRACK1_NOTES, DISH_TRACK2_DATE_FROM,null, DISH_TRACK1_IS_ACTUAL);

        List<DishesTracking> actualTracks = dishesTrackingRepository.findAll();

        assertThat(actualTracks).hasSize(2);
        assertThat(actualTracks).contains(track1);
        assertThat(actualTracks).contains(track2);
    }

    @Test
    void whenUpdateDishesTracking_thenChangesPersisted() {
        DishesTracking originalTrack = createDishesTrackingInDb(null, DISH_TRACK1_DATE_FROM, null, true);
        long originalDishId = originalTrack.getKey().getDishId();
        LocalDate originalDateFrom = originalTrack.getKey().getDateFrom();
        ZonedDateTime originalCreation = originalTrack.getCreatedAt();

        updateDishesTrackingInDb(originalDishId, originalDateFrom);        
        Optional<DishesTracking> updatedTrackOptional = dishesTrackingRepository.findByKeyDishIdAndKeyDateFrom(originalDishId, originalDateFrom);

        assertThat(updatedTrackOptional).isPresent();

        DishesTracking actualUpdatedTrack = updatedTrackOptional.get();

        assertThat(actualUpdatedTrack.getDateTo()).isEqualTo(DISH_TRACK1_DATE_TO);
        assertThat(actualUpdatedTrack.getNotes()).isEqualTo(DISH_TRACK1_NOTES);
        assertThat(actualUpdatedTrack.getCreatedAt()).isEqualTo(originalCreation);
        assertThat(actualUpdatedTrack.getKey().getDishId()).isEqualTo(originalDishId);
        assertThat(actualUpdatedTrack.getKey().getDateFrom()).isEqualTo(originalDateFrom);
        assertThat(actualUpdatedTrack.getUpdatedAt()).isNotNull();
    }

    @Transactional
    private void updateDishesTrackingInDb(long dishId, LocalDate dateFrom) {
        DishesTracking trackToUpdate = dishesTrackingRepository.findByKeyDishIdAndKeyDateFrom(dishId, dateFrom).orElseThrow();
        trackToUpdate.setDateTo(DISH_TRACK1_DATE_TO);
        trackToUpdate.setNotes(DISH_TRACK1_NOTES);

        dishesTrackingRepository.saveAndFlush(trackToUpdate);
    }

    @Test
    public void whenDeleteByDIshesTrackingKey_thenDishesTrackingRemoved() {
        DishesTracking track1 = createDishesTrackingInDb(DISH_TRACK1_NOTES, DISH_TRACK1_DATE_FROM,DISH_TRACK1_DATE_TO, DISH_TRACK1_IS_ACTUAL);
        DishesTracking track2 = createDishesTrackingInDb(DISH_TRACK1_NOTES, DISH_TRACK2_DATE_FROM,null, DISH_TRACK1_IS_ACTUAL);
        long originalDishId = track2.getKey().getDishId();
        LocalDate originalDateFrom = track2.getKey().getDateFrom();

        assertThat(dishesTrackingRepository.findAll()).hasSize(2);

        dishesTrackingRepository.deleteByKeyDishIdAndKeyDateFrom(track1.getKey().getDishId(), DISH_TRACK1_DATE_FROM);

        // Detach the entity and save changes to db
        testEntityManager.flush();
        testEntityManager.clear();

        List<DishesTracking> actualRemainedTracks = dishesTrackingRepository.findAll();

        assertThat(actualRemainedTracks).hasSize(1);
        assertThat(actualRemainedTracks.get(0).getKey().getDishId()).isEqualTo(originalDishId);
        assertThat(actualRemainedTracks.get(0).getKey().getDateFrom()).isEqualTo(originalDateFrom);
    }

}
