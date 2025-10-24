package org.petprojec.familymenu_restapi.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.petprojec.familymenu_restapi.dto.DishDTO;
import org.petprojec.familymenu_restapi.model.Dish;
import org.petprojec.familymenu_restapi.services.DishesService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/dishes")
@ComponentScan(basePackages = {"org.petprojec.familymenu_restapi.services"})
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
        return ResponseEntity.ok(dishList.stream().map(Dish::getDishDTO).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody @Valid DishDTO dish) {
        long id = dishesService.save(dish);
        try {
            return ResponseEntity.created(new URI(String.format("http://localhost:8090/api/v1/dishes/%d",id))).build();    
        } catch (URISyntaxException e) {
            return ResponseEntity.ok(id);
        }
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> processDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable long id) {
        dishesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
