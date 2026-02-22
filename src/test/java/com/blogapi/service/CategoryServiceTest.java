package com.blogapi.service;

import com.blogapi.model.dto.CategoryRequest;
import com.blogapi.model.dto.CategoryResponse;
import com.blogapi.model.entity.Category;
import com.blogapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryRequest categoryRequest;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Technology");
        category.setDescription("Tech posts");

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Technology");
        categoryRequest.setDescription("Tech posts");
    }

    @Test
    void createCategory_ShouldReturnCategoryResponse() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponse response = categoryService.createCategory(categoryRequest);

        assertNotNull(response);
        assertEquals(category.getName(), response.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void getCategoryById_ShouldReturnCategoryResponse() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        CategoryResponse response = categoryService.getCategoryById(1L);

        assertNotNull(response);
        assertEquals(category.getName(), response.getName());
    }
}
