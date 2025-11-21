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
        LOGGER.debug("findByDishIdAndDateFrom() started with params dishId={}, dateFrom={}", dishId, dateFrom);
        Optional<DishesTracking> dishTrack = dishesTrackingRepository.findByKeyDishIdAndKeyDateFrom(dishId, dateFrom);
        LOGGER.info("findByDishIdAndDateFrom() tracking with dishId={}, dateFrom={}" + (dishTrack.isPresent() ? " found": " not found"), dishId, dateFrom);
        return dishTrack;
    }

    public List<DishesTracking> findByDishId(long dishId) {
        LOGGER.debug("findByDishId() started with params dishId={}", dishId);
        List<DishesTracking> trackList = dishesTrackingRepository.findByKeyDishId(dishId);
        LOGGER.info("findByDishId() trackings with dishId={}" + (trackList.isEmpty() ? " not found": " found"), dishId);
        return trackList;
    }

    @Transactional
    public void save(DishesTrackingDTO dishTracking) throws DataIntegrityViolationException {
         LOGGER.debug("save() started with params trackDTO={}", dishTracking);
        try {
            dishesTrackingRepository.saveAndFlush(new DishesTracking(dishTracking));
            LOGGER.info("save() data are added to data storage");
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("save() data are not saved in data storage: {}", e.getMessage());
            throw new DataIntegrityViolationException(
                        String.format("The dish tracking with the id=\"%d\" and date_from=\"%s\" already exists", dishTracking.getDishId(), dishTracking.getDateFrom())
            );
        }
    }

    @Transactional
    public DishesTracking patch(long dishId, LocalDate dateFrom, Optional<LocalDate> dateTo, Optional<String> notes, Optional<Boolean> isActual) {
        LOGGER.debug("patch() started with dishId={}, dateFrom={}, dateTo={}, notes=\"{}\", isActual={}", dishId, dateFrom, dateTo, notes, isActual);
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
                LOGGER.info("patch() data are saved in data storage");
                return dishTrack;
           }
            catch (Exception e) {
                LOGGER.error("save() data are not saved in data storage: {}", e.getMessage());
                throw e;
            }
        } else {
            LOGGER.error("patch() data are not saved in data storage: Dish tracking item with dishId={} and dateFrom={} not found",dishId,dateFrom);
            throw new EntityNotFoundException(String.format("Dish tracking item with dishId=%d and dateFrom=%s does not exist", dishId, dateFrom));
        }
    }

    @Transactional
    public void deleteByDishIdAndDateFrom(long dishId, LocalDate dateFrom) {
        dishesTrackingRepository.deleteByKeyDishIdAndKeyDateFrom(dishId, dateFrom);
    }
}
