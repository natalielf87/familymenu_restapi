package org.petprojec.familymenu_restapi.repositories;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.petprojec.familymenu_restapi.model.Dish;
import org.petprojec.familymenu_restapi.model.DishesTracking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class DishesTrackingRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private DishesTrackingRepository dishesTrackingRepository;

    private static final String DISH1_NAME = "Pasta Bolognese";
    private static final int DISH1_TYPE = 2;
    private static final String DISH1_DESCRIPTION = ":-)";
    // private static final String DISH2_NAME = "Onion Soup";
    // private static final int DISH2_TYPE = 1;

    private static final long DISH_TRACK1_DISH_ID = 1L;
    private static final String DISH_TRACK1_NOTES = "Notes for the dish tracking 1";
    private static final LocalDate DISH_TRACK1_DATE_FROM = LocalDate.now();
    private static final LocalDate DISH_TRACK1_DATE_TO = LocalDate.now().plusDays(1);
    private static final boolean DISH_TRACK1_IS_ACTUAL = true;

    private DishesTracking createDishesTrackingInDb(String notes, LocalDate dateFrom, LocalDate dateTo, boolean isActual) {
        Dish dish = new Dish(DISH1_NAME, DISH1_TYPE, DISH1_DESCRIPTION, true);
        Dish insertedInDbDish = testEntityManager.persistFlushFind(dish);
        DishesTracking track = new DishesTracking(insertedInDbDish.getId(),notes,dateFrom,dateTo,isActual);
        track.setDish(insertedInDbDish);
        return testEntityManager.persistFlushFind(track);
    }

    @Test
    public void whenFindByDishIdAndDateFrom_thenDishesTrackingReturned() {
        DishesTracking track1 = createDishesTrackingInDb(DISH_TRACK1_NOTES, DISH_TRACK1_DATE_FROM, DISH_TRACK1_DATE_TO, DISH_TRACK1_IS_ACTUAL);

        Optional<DishesTracking> actual = dishesTrackingRepository.findByKeyDishIdAndKeyDateFrom(DISH_TRACK1_DISH_ID,DISH_TRACK1_DATE_FROM);

        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(track1);
    }
}
