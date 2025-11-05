package org.petprojec.familymenu_restapi.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.petprojec.familymenu_restapi.dto.DishDTO;
import org.petprojec.familymenu_restapi.dto.DishesDTOUpdate;
import org.petprojec.familymenu_restapi.model.Dish;
import org.petprojec.familymenu_restapi.services.DishesService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/dishes")
public class DishesController {
    private final DishesService dishesService;

    public DishesController(DishesService dishesService) {
        this.dishesService = dishesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<DishDTO>> findAll() {
        List<Dish> dishList = dishesService.findAll();
        if (dishList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dishList.stream().map(Dish::getDishDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDTO> findById(@PathVariable long id) {
        Optional<Dish> dishOpt = dishesService.findById(id);
        if (dishOpt.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dishOpt.get().getDishDTO());
    }

    @GetMapping
    public ResponseEntity<List<DishDTO>> findByNameStartingWith(@RequestParam(name="name", required =true) String name) {
        List<Dish> dishList = dishesService.findByNameStartingWith(name);
        if (dishList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dishList.stream().map(Dish::getDishDTO).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody @Valid DishDTO dish) {
        long id = dishesService.save(dish);
        try {
            return ResponseEntity.created(new URI(String.format("/api/v1/dishes/%d",id))).build();    
        } catch (URISyntaxException e) {
            return ResponseEntity.ok(id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishDTO> update(@PathVariable long id, @RequestBody @Valid DishDTO dishDTO) {
        return ResponseEntity.ok(dishesService.update(id, dishDTO.getName(), dishDTO.getType(), dishDTO.getDescription(), dishDTO.getIsActual()).getDishDTO());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DishDTO> patch(@PathVariable long id, @RequestBody @Valid DishesDTOUpdate dishDTO) {
        return ResponseEntity.ok(dishesService.patch(id, 
                                                            Optional.ofNullable(dishDTO.getName()), 
                                                            Optional.ofNullable(dishDTO.getType()), 
                                                            Optional.ofNullable(dishDTO.getDescription()), 
                                                            Optional.ofNullable(dishDTO.getIsActual())
                                                            ).getDishDTO()
                                        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> processDataIntegrityViolationException(Exception e) {
        return new ResponseEntity<String>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> processMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getMessage();
        if (message.contains("field 'type'")) {
            return ResponseEntity.badRequest().body("Type must be not negative and less than or equal to 3");
        } else if (message.contains("field 'name'")) {
            return ResponseEntity.badRequest().body("Name must be not null and not blank and size <= 100");
        } else if (message.contains("field 'description'")) {
            return ResponseEntity.badRequest().body("Description must not be longer than 500 characters");
        } else if (message.contains("field 'IsActual'")) {
            return ResponseEntity.badRequest().body("IsActual must have not null valid boolean value");
        }
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> processEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<> (e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable long id) {
        dishesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteByName(@RequestParam(name="name", required =true) String name) {
        dishesService.deleteByName(name);
        return ResponseEntity.noContent().build();
    }
    

}
