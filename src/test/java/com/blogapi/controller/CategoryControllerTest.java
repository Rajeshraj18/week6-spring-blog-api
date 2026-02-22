package com.blogapi.controller;

import com.blogapi.model.dto.CategoryRequest;
import com.blogapi.model.dto.CategoryResponse;
import com.blogapi.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@SuppressWarnings("null")
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryResponse categoryResponse;
    private CategoryRequest categoryRequest;

    @BeforeEach
    void setUp() {
        categoryResponse = CategoryResponse.builder()
                .id(1L)
                .name("Technology")
                .description("Tech posts")
                .build();

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Technology");
        categoryRequest.setDescription("Tech posts");
    }

    @Test
    void getAllCategories_ShouldReturn200() throws Exception {
        List<CategoryResponse> categories = Arrays.asList(categoryResponse);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Technology"));
    }

    @Test
    void getCategoryById_ShouldReturn200() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryResponse);

        mockMvc.perform(get("/api/categories/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Technology"));
    }

    @Test
    void createCategory_ShouldReturn201() throws Exception {
        when(categoryService.createCategory(any(CategoryRequest.class))).thenReturn(categoryResponse);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Technology"));
    }

    @Test
    void updateCategory_ShouldReturn200() throws Exception {
        when(categoryService.updateCategory(eq(1L), any(CategoryRequest.class))).thenReturn(categoryResponse);
        mockMvc.perform(put("/api/categories/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Technology"));
    }

    @Test
    void deleteCategory_ShouldReturn204() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
