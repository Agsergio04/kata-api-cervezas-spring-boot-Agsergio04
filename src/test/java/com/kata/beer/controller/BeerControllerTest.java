package com.kata.beer.controller;

import com.kata.beer.model.Beer;
import com.kata.beer.model.Brewery;
import com.kata.beer.model.Category;
import com.kata.beer.model.Style;
import com.kata.beer.service.BeerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BeerService beerService;

    private Beer testBeer;
    private Brewery testBrewery;
    private Category testCategory;
    private Style testStyle;


    @BeforeEach
    void setUp() {
        testBrewery = new Brewery();
        testBrewery.setId(1);
        testBrewery.setName("Test Brewery");

        testCategory = new Category();
        testCategory.setId(1);
        testCategory.setCatName("Test Category");

        testStyle = new Style();
        testStyle.setId(1);
        testStyle.setStyleName("Test Style");
        testStyle.setCatId(1);

        testBeer = new Beer();
        testBeer.setId(1);
        testBeer.setName("Test Beer");
        testBeer.setBrewery(testBrewery);
        testBeer.setCategory(testCategory);
        testBeer.setStyle(testStyle);
        testBeer.setAbv(5.5f);
        testBeer.setIbu(30.0f);
        testBeer.setSrm(15.0f);
        testBeer.setUpc(123456);
        testBeer.setFilepath("/path/to/beer.jpg");
        testBeer.setDescript("Test Description");
        testBeer.setAddUser(0);
        testBeer.setLastMod(LocalDateTime.now());
    }

    @Test
    void getAllBeers_ShouldReturnListOfBeers() throws Exception {
        List<Beer> beers = Collections.singletonList(testBeer);
        when(beerService.getAllBeers()).thenReturn(beers);

        mockMvc.perform(get("/api/beers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Beer")))
                .andExpect(jsonPath("$[0].brewery.name", is("Test Brewery")));

        verify(beerService, times(1)).getAllBeers();
    }

    @Test
    void getBeerById_WhenBeerExists_ShouldReturnBeer() throws Exception {
        when(beerService.getBeerById(1)).thenReturn(testBeer);

        mockMvc.perform(get("/api/beer/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Beer")))
                .andExpect(jsonPath("$.abv", is(5.5)));

        verify(beerService, times(1)).getBeerById(1);
    }

    @Test
    void createBeer_WithValidData_ShouldReturnCreatedBeer() throws Exception {
        when(beerService.createBeer(ArgumentMatchers.any(Beer.class))).thenReturn(testBeer);

        mockMvc.perform(post("/api/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBeer)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Test Beer")))
                .andExpect(jsonPath("$.id", is(1)));

        verify(beerService, times(1)).createBeer(ArgumentMatchers.any(Beer.class));
    }

    @Test
    void updateBeer_WithValidData_ShouldReturnUpdatedBeer() throws Exception {
        Beer updatedBeer = new Beer();
        updatedBeer.setId(1);
        updatedBeer.setName("Updated Beer");
        updatedBeer.setBrewery(testBrewery);
        updatedBeer.setCategory(testCategory);
        updatedBeer.setStyle(testStyle);

        when(beerService.updateBeer(eq(1), ArgumentMatchers.any(Beer.class))).thenReturn(updatedBeer);

        mockMvc.perform(put("/api/beer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBeer)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Updated Beer")));

        verify(beerService, times(1)).updateBeer(eq(1), ArgumentMatchers.any(Beer.class));
    }

    @Test
    void partialUpdateBeer_WithValidData_ShouldReturnUpdatedBeer() throws Exception {
        Beer partialBeer = new Beer();
        partialBeer.setName("Partially Updated Beer");

        Beer updatedBeer = new Beer();
        updatedBeer.setId(1);
        updatedBeer.setName("Partially Updated Beer");
        updatedBeer.setBrewery(testBrewery);
        updatedBeer.setCategory(testCategory);
        updatedBeer.setStyle(testStyle);

        when(beerService.partialUpdateBeer(eq(1), ArgumentMatchers.any(Beer.class))).thenReturn(updatedBeer);

        mockMvc.perform(patch("/api/beer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialBeer)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Partially Updated Beer")));

        verify(beerService, times(1)).partialUpdateBeer(eq(1), ArgumentMatchers.any(Beer.class));
    }

    @Test
    void deleteBeer_ShouldReturnNoContent() throws Exception {
        doNothing().when(beerService).deleteBeer(1);

        mockMvc.perform(delete("/api/beer/1"))
                .andExpect(status().isNoContent());

        verify(beerService, times(1)).deleteBeer(1);
    }
}

