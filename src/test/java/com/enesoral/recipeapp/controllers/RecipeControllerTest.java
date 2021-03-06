package com.enesoral.recipeapp.controllers;

import com.enesoral.recipeapp.converters.CategoryConverter;
import com.enesoral.recipeapp.domain.Recipe;
import com.enesoral.recipeapp.dto.RecipeDto;
import com.enesoral.recipeapp.services.CategoryService;
import com.enesoral.recipeapp.services.RecipeService;
import com.enesoral.recipeapp.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    CategoryService categoryService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    RecipeController recipeController;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeController = new RecipeController(recipeService, categoryService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ExceptionHandlingController())
                .build();
    }

    @Test
    public void showById() throws Exception {
        final Long ID = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.findById(ID)).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show/"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show-recipe"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void getRecipeNumberFormatException() throws Exception {
        mockMvc.perform(get("/recipe/asd/show/"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
    }

    @Test
    public void testPostNewRecipeForm() throws Exception {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(2L);

        when(recipeService.saveRecipeDto(any())).thenReturn(recipeDto);

        mockMvc.perform(post("/recipe/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string")
                .param("directions", "some directions")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));
    }

    @Test
    public void testPostNewRecipeFormValidationFail() throws Exception {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(2L);

        when(recipeService.saveRecipeDto(any())).thenReturn(recipeDto);

        mockMvc.perform(post("/recipe/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", ""))
                        .andExpect(status().isBadRequest());
    }
}
