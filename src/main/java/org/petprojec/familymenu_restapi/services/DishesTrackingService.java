package org.petprojec.familymenu_restapi.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.petprojec.familymenu_restapi.dto.DishesTrackingDTO;
import org.petprojec.familymenu_restapi.model.DishesTracking;
import org.petprojec.familymenu_restapi.repositories.DishesTrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class DishesTrackingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DishesTrackingService.class);
    private final DishesTrackingRepository dishesTrackingRepository;

    public DishesTrackingService(DishesTrackingRepository dishesTrackingRepository) {
        this.dishesTrackingRepository = dishesTrackingRepository;
    }

    public Optional<DishesTracking> findByDishIdAndDateFrom(long dishId, LocalDate dateFrom) {
        LOGGER.debug(String.format("findByDishIdAndDateFrom() started with params dishId=%d, dateFrom=%s", dishId, dateFrom));
        Optional<DishesTracking> dishTrack = dishesTrackingRepository.findByKeyDishIdAndKeyDateFrom(dishId, dateFrom);
        LOGGER.info(String.format("findByDishIdAndDateFrom() tracking with dishId=%d, dateFrom=%s", dishId, dateFrom) + (dishTrack.isPresent() ? " found": " not found"));
        return dishTrack;
    }

    public List<DishesTracking> findByDishId(long dishId) {
        LOGGER.debug(String.format("findByDishId() started with params dishId=%d", dishId));
        List<DishesTracking> trackList = dishesTrackingRepository.findByKeyDishId(dishId);
        LOGGER.info(String.format("findByDishId() trackings with dishId=%d", dishId) + (trackList.isEmpty() ? " not found": " found"));
        return trackList;
    }

    @Transactional
    public void save(DishesTrackingDTO dishTracking) throws DataIntegrityViolationException {
         LOGGER.debug(String.format("save() started with params trackDTO=%s", dishTracking));
        try {
            dishesTrackingRepository.saveAndFlush(new DishesTracking(dishTracking));
            LOGGER.info(String.format("save() data are added to data storage"));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error(String.format("save() data are not saved in data storage: %s", e.getMessage()));
            throw new DataIntegrityViolationException(
                        String.format("The dish tracking with the id=\"%d\" and date_from=\"%s\" already exists", dishTracking.getDishId(), dishTracking.getDateFrom())
            );
        }
    }

    @Transactional
    public DishesTracking patch(long dishId, LocalDate dateFrom, Optional<LocalDate> dateTo, Optional<String> notes, Optional<Boolean> isActual) {
        LOGGER.debug(String.format("patch() started with dishId=%d, dateFrom=%s, dateTo=%s, notes=\"%s\", isActual=%b", dishId, dateFrom, dateTo, notes, isActual));
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
           try {
                DishesTracking dishTrack = dishesTrackingRepository.saveAndFlush(track);
                LOGGER.info(String.format("patch() data are saved in data storage"));
                return dishTrack;
           }
            catch (Exception e) {
                LOGGER.error(String.format("save() data are not saved in data storage: %s", e.getMessage()));
                throw e;
            }
        } else {
            LOGGER.error(String.format("patch() data are not saved in data storage: Dish tracking item with dishId=%d and dateFrom=%s not found",dishId,dateFrom));
            throw new EntityNotFoundException(String.format("Dish tracking item with dishId=%d and dateFrom=%s does not exist", dishId, dateFrom));
        }
    }

    @Transactional
    public void deleteByDishIdAndDateFrom(long dishId, LocalDate dateFrom) {
        dishesTrackingRepository.deleteByKeyDishIdAndKeyDateFrom(dishId, dateFrom);
    }
}
