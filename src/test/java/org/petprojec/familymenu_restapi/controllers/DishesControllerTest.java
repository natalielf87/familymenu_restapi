package org.petprojec.familymenu_restapi.controllers;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.petprojec.familymenu_restapi.dto.DishDTO;
import org.petprojec.familymenu_restapi.model.Dish;
import org.petprojec.familymenu_restapi.services.DishesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DishesController.class)
public class DishesControllerTest {
    
    @MockitoBean
    private DishesService dishesService;

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/dishes";

    private Dish testDish;
    private DishDTO testDishDTO;

    static final long ID1 = 1L;
    static final long ID2 = 2L;

    static final String DISH1_NAME = "Meatballs";
    private static final String DISH1_DESCRIPTION = ":-)";

    @BeforeEach
    public void init(){
        testDish = new Dish(DISH1_NAME, 0, DISH1_DESCRIPTION);
        testDish.setId(ID1);
        testDishDTO = new DishDTO(DISH1_NAME, 0, DISH1_DESCRIPTION, true);
    }

    @Test
    public void whenFindAll_thenReturn200AndAllDishes() throws Exception{
        when(dishesService.findAll()).thenReturn(List.of(testDish));
        this.mockMvc.perform(get(BASE_URL.concat("/all"))).andDo(print())
                .andExpect(status().isOk());
    }
    
}