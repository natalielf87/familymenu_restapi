package org.petprojec.familymenu_restapi.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.petprojec.familymenu_restapi.dto.DishDTO;
import org.petprojec.familymenu_restapi.dto.patch.DishPatchDTO;
import org.petprojec.familymenu_restapi.model.Dish;
import org.petprojec.familymenu_restapi.services.DishesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = DishesController.class)
public class DishesControllerTest {
    
    @MockitoBean
    private DishesService dishesService;

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/dishes";

    private Dish testDish;
    private DishDTO testDishDTO;

    private Dish testDish2;
    private DishDTO testDishDTO2;

    static final long ID1 = 1L;

    private static final String DISH1_NAME = "Meatballs";
    private static final String DISH1_DESCRIPTION = ":-)";
    private static final int DISH1_TYPE = 2;

    private static final String DISH2_NAME = "Meatballs changed";
    private static final String DISH2_DESCRIPTION = ":-) changed";
    private static final int DISH2_TYPE = 0;
    private static final boolean DISH2_ISACTUAL = false;


    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void initBeforeAll() {
        objectMapper.registerModule(new Jdk8Module());
    }

    @BeforeEach
    public void init(){
        testDish = new Dish(DISH1_NAME, DISH1_TYPE, DISH1_DESCRIPTION);
        testDish.setId(ID1);
        testDishDTO = testDish.getDishDTO();

        testDishDTO2 = new DishDTO(DISH2_NAME, DISH2_TYPE, DISH2_DESCRIPTION, DISH2_ISACTUAL);
        testDish2 = new Dish(testDishDTO2);
        testDish2.setId(ID1);
    }

    @Test
    public void whenFindAll_thenReturn200AndAllDishes() throws Exception{
        when(dishesService.findAll()).thenReturn(List.of(testDish));
        this.mockMvc.perform(get(BASE_URL.concat("/all")).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("[0].name").value(DISH1_NAME))
                .andExpect(jsonPath("[0].type").value(DISH1_TYPE))
                .andExpect(jsonPath("[0].description").value(DISH1_DESCRIPTION))
                .andExpect(jsonPath("[0].isActual").value(true));
        
        verify(dishesService, times(1)).findAll();
    }

    @Test
    public void whenFindAll_thenReturn204AndNoDishes() throws Exception{
        when(dishesService.findAll()).thenReturn(Collections.emptyList());
        this.mockMvc.perform(get(BASE_URL.concat("/all")).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(dishesService, times(1)).findAll();
    }
    
    @Test
    public void whenFindById_thenReturn200AndSingleDish() throws Exception{
        when(dishesService.findById(anyLong())).thenReturn(Optional.ofNullable(testDish));
        this.mockMvc.perform(get(BASE_URL.concat("/").concat(Long.toString(ID1))).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(DISH1_NAME))
                .andExpect(jsonPath("$.type").value(DISH1_TYPE))
                .andExpect(jsonPath("$.description").value(DISH1_DESCRIPTION))
                .andExpect(jsonPath("$.isActual").value(true));
        verify(dishesService, times(1)).findById(anyLong());
    }

    @Test
    public void whenFindById_thenReturn204AndNoDishes() throws Exception{
        when(dishesService.findById(anyLong())).thenReturn(Optional.empty());
        this.mockMvc.perform(get(BASE_URL.concat("/").concat(Long.toString(ID1))).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(dishesService, times(1)).findById(anyLong());
    }

    @Test
    public void whenFindByNameStartingWith_thenReturn200AndSingleDish() throws Exception{
        when(dishesService.findByNameStartingWith(anyString())).thenReturn(List.of(testDish));
        this.mockMvc.perform(get(BASE_URL.concat("?name=").concat(DISH1_NAME.substring(0, 3))).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("[0].name").value(DISH1_NAME))
                .andExpect(jsonPath("[0].type").value(DISH1_TYPE))
                .andExpect(jsonPath("[0].description").value(DISH1_DESCRIPTION))
                .andExpect(jsonPath("[0].isActual").value(true));
        verify(dishesService, times(1)).findByNameStartingWith(anyString());
    }

    @Test
    public void whenFindByNameStartingWith_thenReturn204AndNoDishes() throws Exception{
        when(dishesService.findByNameStartingWith(anyString())).thenReturn(List.of());
        this.mockMvc.perform(get(BASE_URL.concat("?name=").concat(DISH1_NAME.substring(0, 3))).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(dishesService, times(1)).findByNameStartingWith(anyString());
    }

    @Test
    public void whenSave_thenNewIdIsReturned() throws Exception {
        long expectedID = ID1;
        when(dishesService.save(any(DishDTO.class))).thenReturn(expectedID);
        String expectedLocation =  BASE_URL.concat("/").concat(String.valueOf(expectedID));
        this.mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testDishDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", expectedLocation))
                .andExpect(content().string(String.valueOf(expectedID)));
        
        verify(dishesService, times(1)).save(any(DishDTO.class));
        
    }

    @Test
    public void whenUpdate_thenUpdatedDishReturned() throws Exception {
        when(dishesService.update(eq(ID1), anyString(),anyInt(), anyString(), anyBoolean())).thenReturn(testDish2);
        
        this.mockMvc.perform(
                        put(BASE_URL.concat("/").concat(String.valueOf(ID1)))
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testDishDTO2))
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(DISH2_NAME))
                .andExpect(jsonPath("$.type").value(DISH2_TYPE))
                .andExpect(jsonPath("$.description").value(DISH2_DESCRIPTION))
                .andExpect(jsonPath("$.isActual").value(DISH2_ISACTUAL));
        
        verify(dishesService, times(1)).update(eq(ID1), anyString(),anyInt(), anyString(), anyBoolean());
        
    }

    @Test
    public void whenUpdateNameMissing_thenErrorMessageReturned() throws Exception {

        DishDTO updatedDishDTO = new DishDTO();
        updatedDishDTO.setType(DISH1_TYPE);
        updatedDishDTO.setIsActual(true);
        
        this.mockMvc.perform(
                        put(BASE_URL.concat("/").concat(String.valueOf(ID1)))
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedDishDTO))
                        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Name must be not null and not blank and size <= 100"));
        
        verify(dishesService, times(0)).update(anyLong(), anyString(),anyInt(), anyString(), anyBoolean());
        
    }

    @Test
    public void whenUpdateIdMissing_thenErrorMessageReturned() throws Exception {

        DishDTO updatedDishDTO = new DishDTO();
        updatedDishDTO.setName(DISH1_NAME);
        updatedDishDTO.setType(DISH1_TYPE);
        updatedDishDTO.setIsActual(true);
        
        this.mockMvc.perform(
                        put(BASE_URL.concat("/"))
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedDishDTO))
                        )
                .andDo(print())
                .andExpect(status().isNotFound());
        
        verify(dishesService, times(0)).update(anyLong(), anyString(),anyInt(), anyString(), anyBoolean());
        
    }

    @Test
    public void whenUpdateDishNotFound_thenErrorMessageReturned() throws Exception {

        DishDTO updatedDishDTO = new DishDTO();
        updatedDishDTO.setName(DISH1_NAME);
        updatedDishDTO.setType(DISH1_TYPE);
        updatedDishDTO.setIsActual(true);
        updatedDishDTO.setDescription(DISH1_DESCRIPTION);

        String expectedMessage = String.format("Dish item with id=%d does not exist", ID1);

        when(dishesService.update(anyLong(), anyString(),anyInt(), anyString(), anyBoolean()))
            .thenThrow(new EntityNotFoundException(expectedMessage));
        
        this.mockMvc.perform(
                        put(BASE_URL.concat("/").concat(String.valueOf(ID1)))
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedDishDTO))
                        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedMessage));
        
        verify(dishesService, times(1)).update(eq(ID1), anyString(),anyInt(), anyString(), anyBoolean());
        
    }

    @Test
    public void whenPatchTypeAndIsActual_thenUpdatedDishReturned() throws Exception {
        final int newType = 3;
        final boolean newIsActual = false;
        Dish expectedDish = new Dish(DISH1_NAME, newType, DISH1_DESCRIPTION, newIsActual);
        expectedDish.setId(ID1);

        DishPatchDTO patchDishDTO = new DishPatchDTO();
        patchDishDTO.setType(Optional.of(newType));
        patchDishDTO.setIsActual(Optional.of(newIsActual));

        when(
                dishesService.patch(
                        eq(ID1), eq(Optional.empty()), eq(Optional.of(newType)), eq(Optional.empty()), eq(Optional.of(newIsActual))
                        )
            ).thenReturn(expectedDish);
        
        this.mockMvc.perform(
                        patch(BASE_URL.concat("/").concat(String.valueOf(ID1)))
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(patchDishDTO))
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(DISH1_NAME))
                .andExpect(jsonPath("$.type").value(newType))
                .andExpect(jsonPath("$.description").value(DISH1_DESCRIPTION))
                .andExpect(jsonPath("$.isActual").value(newIsActual));
        
        verify(dishesService, times(1))
                .patch(eq(ID1), eq(Optional.empty()), eq(Optional.of(newType)), eq(Optional.empty()), eq(Optional.of(newIsActual)));
        
    }

    @Test
    public void whenDeleteById_thenNoContentReturned() throws Exception {
        doNothing().when(dishesService).deleteById(eq(ID1));
        
        this.mockMvc.perform(
                delete(BASE_URL.concat("/").concat(String.valueOf(ID1)))
                )
        .andDo(print())
        .andExpect(status().isNoContent());

        verify(dishesService, times(1)).deleteById(eq(ID1));
        
    }

    @Test
    public void whenDeleteByIdNonExistent_thenNoContentReturned() throws Exception {
        final long nonExistentId = 9999l;
        doNothing().when(dishesService).deleteById(eq(nonExistentId));
        
        this.mockMvc.perform(
                delete(BASE_URL.concat("/").concat(String.valueOf(nonExistentId)))
                )
        .andDo(print())
        .andExpect(status().isNoContent());

        verify(dishesService, times(1)).deleteById(eq(nonExistentId));
    }

    @Test
    public void whenDeleteByName_thenNoContentReturned() throws Exception {
        doNothing().when(dishesService).deleteByName(eq(DISH1_NAME));

        this.mockMvc.perform(
                delete(BASE_URL.concat("?name=").concat(String.valueOf(DISH1_NAME)))
                )
        .andDo(print())
        .andExpect(status().isNoContent());

        verify(dishesService, times(1)).deleteByName(eq(DISH1_NAME));
        
    }

    @Test
    public void whenDeleteByNameNonExistent_thenNoContentReturned() throws Exception {
        final String nonExistentName = "NonExistentDish";
        doNothing().when(dishesService).deleteByName(eq(nonExistentName));
        
        this.mockMvc.perform(
                delete(BASE_URL.concat("?name=").concat(nonExistentName))
                )
        .andDo(print())
        .andExpect(status().isNoContent());

        verify(dishesService, times(1)).deleteByName(eq(nonExistentName));
    }
}