package com.kata.beer.controller;

import com.kata.beer.model.Category;
import com.kata.beer.service.CategoryService;
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

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private CategoryService categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1);
        testCategory.setCatName("Test Category");
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() throws Exception {
        List<Category> categories = Collections.singletonList(testCategory);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].catName", is("Test Category")));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategory() throws Exception {
        when(categoryService.getCategoryById(1)).thenReturn(testCategory);

        mockMvc.perform(get("/api/categorie/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.catName", is("Test Category")));

        verify(categoryService, times(1)).getCategoryById(1);
    }
}

