package org.petprojec.familymenu_restapi.controllers;

import java.util.List;

import org.petprojec.familymenu_restapi.model.Dish;
import org.petprojec.familymenu_restapi.services.DishesService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dishes")
@ComponentScan(basePackages = {"org.petprojec.familymenu_restapi.services"})
public class DishesController {
    private final DishesService dishesService;

    public DishesController(DishesService dishesService) {
        this.dishesService = dishesService;
    }

    @GetMapping("/all")
    public List<Dish> findAll() {
        return dishesService.findAll();
    }

    @GetMapping("/?name=")
    @ResponseStatus(HttpStatus.OK)
    public List<Dish> findByNameStartingWith(@RequestParam String name) {
        return dishesService.findByNameStartingWith(name);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public long save(@RequestBody Dish dish) {
        return dishesService.save(dish);
    }
}
