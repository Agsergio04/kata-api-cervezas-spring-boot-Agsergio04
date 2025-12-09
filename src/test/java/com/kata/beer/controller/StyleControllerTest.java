package com.kata.beer.controller;

import com.kata.beer.model.Style;
import com.kata.beer.service.StyleService;
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

@WebMvcTest(StyleController.class)
class StyleControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private StyleService styleService;

    private Style testStyle;

    @BeforeEach
    void setUp() {
        testStyle = new Style();
        testStyle.setId(1);
        testStyle.setStyleName("Test Style");
        testStyle.setCatId(1);
    }

    @Test
    void getAllStyles_ShouldReturnListOfStyles() throws Exception {
        List<Style> styles = Collections.singletonList(testStyle);
        when(styleService.getAllStyles()).thenReturn(styles);

        mockMvc.perform(get("/api/styles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].styleName", is("Test Style")));

        verify(styleService, times(1)).getAllStyles();
    }

    @Test
    void getStyleById_WhenStyleExists_ShouldReturnStyle() throws Exception {
        when(styleService.getStyleById(1)).thenReturn(testStyle);

        mockMvc.perform(get("/api/style/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.styleName", is("Test Style")))
                .andExpect(jsonPath("$.catId", is(1)));

        verify(styleService, times(1)).getStyleById(1);
    }
}

