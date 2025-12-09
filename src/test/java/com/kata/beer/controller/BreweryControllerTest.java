package com.kata.beer.controller;

import com.kata.beer.model.Brewery;
import com.kata.beer.service.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(BreweryController.class)
class BreweryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BreweryService breweryService;

    private Brewery testBrewery;

    @BeforeEach
    void setUp() {
        testBrewery = new Brewery();
        testBrewery.setId(1);
        testBrewery.setName("Test Brewery");
        testBrewery.setCity("Test City");
        testBrewery.setCountry("Test Country");
    }

    @Test
    void getAllBreweries_ShouldReturnListOfBreweries() throws Exception {
        List<Brewery> breweries = Collections.singletonList(testBrewery);
        when(breweryService.getAllBreweries()).thenReturn(breweries);

        mockMvc.perform(get("/api/breweries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Brewery")))
                .andExpect(jsonPath("$[0].city", is("Test City")));

        verify(breweryService, times(1)).getAllBreweries();
    }

    @Test
    void getBreweryById_WhenBreweryExists_ShouldReturnBrewery() throws Exception {
        when(breweryService.getBreweryById(1)).thenReturn(testBrewery);

        mockMvc.perform(get("/api/brewerie/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Brewery")))
                .andExpect(jsonPath("$.country", is("Test Country")));

        verify(breweryService, times(1)).getBreweryById(1);
    }
}

