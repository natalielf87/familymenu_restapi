package org.petprojec.familymenu_restapi.services;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.petprojec.familymenu_restapi.dto.DishesTrackingDTO;
import org.petprojec.familymenu_restapi.model.DishesTracking;
import org.petprojec.familymenu_restapi.repositories.DishesTrackingRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class DishesTrackingService {
    private final DishesTrackingRepository dishesTrackingRepository;

    public DishesTrackingService(DishesTrackingRepository dishesTrackingRepository) {
        this.dishesTrackingRepository = dishesTrackingRepository;
    }

    public Optional<DishesTracking> findByDishIdAndDateFrom(long dishId, LocalDate dateFrom) {
        return dishesTrackingRepository.findByKeyDishIdAndKeyDateFrom(dishId, dateFrom);
    }

    public List<DishesTracking> findByDishId(long dishId) {
        return dishesTrackingRepository.findByKeyDishId(dishId);
    }

    @Transactional
    public void save(DishesTrackingDTO dishTracking) throws DataIntegrityViolationException {
        try {
            //DishesTracking insertedDishTracking = (DishesTracking)
            dishesTrackingRepository.saveAndFlush(new DishesTracking(dishTracking));
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                        String.format("The dish tracking with the id=\"%d\" and date_from=\"%s\" already exists", dishTracking.getDishId(), dishTracking.getDateFrom())
            );
        }
    }

    @Transactional
    public DishesTracking patch(long dishId, LocalDate dateFrom, Optional<LocalDate> dateTo, Optional<String> notes, Optional<Boolean> isActual) {
        Optional<DishesTracking> trackOpt = dishesTrackingRepository.findByKeyDishIdAndKeyDateFrom(dishId,dateFrom);
        if (trackOpt.isPresent()) {
            DishesTracking track = trackOpt.get();
            if (dateTo.isPresent()) {
                track.setDateTo(dateTo.get());
            }
            if (notes.isPresent()) {
                track.setNotes(notes.get());
            }
            if (isActual.isPresent()) {
                track.setActual(isActual.get());
           }
            return dishesTrackingRepository.saveAndFlush(track);
        } else {
            throw new EntityNotFoundException(String.format("Dish tracking item with dishId=%d and dateFrom=%s does not exist", dishId, dateFrom));
        }
    }

    @Transactional
    public void deleteByDishIdAndDateFrom(long dishId, ZonedDateTime dateFrom) {
        dishesTrackingRepository.deleteByKeyDishIdAndKeyDateFrom(dishId, dateFrom);
    }
}
