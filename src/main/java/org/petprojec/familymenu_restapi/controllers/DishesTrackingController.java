package org.petprojec.familymenu_restapi.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.petprojec.familymenu_restapi.dto.DishesTrackingDTO;
import org.petprojec.familymenu_restapi.dto.patch.DishesTrackingPatchDTO;
import org.petprojec.familymenu_restapi.model.DishesTracking;
import org.petprojec.familymenu_restapi.services.DishesTrackingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.DeleteExchange;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/dishestracking/")
public class DishesTrackingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DishesTrackingController.class);
    private final DishesTrackingService dishesTrackingService;

    public DishesTrackingController(DishesTrackingService dishesTrackingService) {
        this.dishesTrackingService = dishesTrackingService;
    }

    @GetMapping
    public ResponseEntity<DishesTrackingDTO> findDishesTrackingDTOByDishIdAndDateFrom(@RequestParam(name="dishId", required =true) long dishId, 
                                                                    @RequestParam(name="fromDate", required=true) LocalDate fromDate) {
        LOGGER.info(String.format("findDishesTrackingDTOByDishIdAndDateFrom() started with params dishId=%d fromDate=%s", dishId,fromDate));                                            
        Optional<DishesTracking> trackOpt = dishesTrackingService.findByDishIdAndDateFrom(dishId, fromDate);
        if (trackOpt.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(trackOpt.get().getDishesTrackingDTO());
    }

    @GetMapping("{dishId}")
    public ResponseEntity<List<DishesTrackingDTO>> findDishesTrackingByDishId(@PathVariable long dishId) {
        LOGGER.info(String.format("findDishesTrackingByDishId() started with params dishId=%d", dishId));
        List<DishesTracking> trackList = dishesTrackingService.findByDishId(dishId);
        if (trackList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(trackList.stream().map(item->item.getDishesTrackingDTO()).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DishesTrackingDTO trackDTO) {
        LOGGER.info(String.format("save() started with params dishesTrackingDTO=%d", trackDTO));
        dishesTrackingService.save(trackDTO);
        try {
            return ResponseEntity.created(
                new URI(
                    String.format("/api/v1/dishestracking/dishId=%d&fromDate=%s",trackDTO.getDishId(),trackDTO.getDateFrom())
                    )
                ).build();    
        } catch (URISyntaxException e) {
            LOGGER.error(String.format("save() problem occured while creating URI: %s", e.getMessage()));
            return ResponseEntity.ok(null);
        }
    }

    @PatchMapping
    public ResponseEntity<DishesTrackingDTO> patch(@RequestParam(name="dishId", required =true) long dishId, 
                                        @RequestParam(name="fromDate", required=true) LocalDate fromDate, 
                                        @RequestBody @Valid DishesTrackingPatchDTO trackPatchDTO) {
        LOGGER.info(String.format("patch() started with params dishId=%d, fromDate=%s, dishesTrackingDTO=%d", dishId, fromDate, trackPatchDTO));
        return ResponseEntity.ok(dishesTrackingService
                                    .patch( dishId, 
                                            fromDate, 
                                            trackPatchDTO.getDateTo(),
                                            trackPatchDTO.getNotes(),
                                            trackPatchDTO.getIsActual()
                                            )
                                    .getDishesTrackingDTO()
                                );
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteByDishIdAndDateFrom(@RequestParam(name="dishId", required =true) long dishId, 
                                        @RequestParam(name="fromDate", required=true) LocalDate fromDate) {
        LOGGER.info(String.format("deleteByDishIdAndDateFrom() started for dishId=%d, fromDate=%s",dishId, fromDate));
        dishesTrackingService.deleteByDishIdAndDateFrom(dishId, fromDate);
        LOGGER.info(String.format("deleteByDishIdAndDateFrom() completed for dishId=%d, fromDate=%s",dishId, fromDate));
        return ResponseEntity.noContent().build();
    }
    
}
