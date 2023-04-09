package com.menu.recipe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.menu.recipe.data.Recipe;
import com.menu.recipe.exception.ResourceNotFoundException;
import com.menu.recipe.service.RecipeService;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

	@Mock
	private RecipeService recipeService;

	@InjectMocks
	private RecipeController recipeController;

	@Autowired
	private MockMvc mockMvc;

	private Recipe recipe = new Recipe(1L, "Chicken Curry", "Cook the chicken and add curry powder", 4,
			"chicken, curry powder", "true");

	private List<Recipe> recipeList = new ArrayList<>(Arrays.asList(
			new Recipe(1L, "Chicken Curry", "Cook the chicken and add curry powder", 4, "chicken, curry powder",
					"false"),
			new Recipe(2L, "Vegetable Soup", "Cook vegetables and blend into soup", 6,
					"carrots, onions, celery, vegetable broth", "true")));

	@Test
	void testGetAllRecipes() throws Exception {
		when(recipeService.getAllRecipes()).thenReturn(recipeList);

		mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

		mockMvc.perform(get("/recipes")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name").value(recipeList.get(0).getName()))
				.andExpect(jsonPath("$[0].instructions").value(recipeList.get(0).getInstructions()))
				.andExpect(jsonPath("$[0].ingredients").value(recipeList.get(0).getIngredients()))
				.andExpect(jsonPath("$[0].servingSize").value(recipeList.get(0).getServingSize()))
				.andExpect(jsonPath("$[0].vegetarian").value(recipeList.get(0).getVegetarian()))
				.andExpect(jsonPath("$[1].name").value(recipeList.get(1).getName()))
				.andExpect(jsonPath("$[1].instructions").value(recipeList.get(1).getInstructions()))
				.andExpect(jsonPath("$[1].ingredients").value(recipeList.get(1).getIngredients()))
				.andExpect(jsonPath("$[1].servingSize").value(recipeList.get(1).getServingSize()))
				.andExpect(jsonPath("$[1].vegetarian").value(recipeList.get(1).getVegetarian()));
	}

	@Test
	public void testUpdateRecipeSuccess() {

		// Update the recipe with new values
		Recipe updatedRecipe = new Recipe(1L, "Chicken Curry", "Cook the chicken and add curry powder", 6,
				"chicken, curry powder", "true");

		// Mock the recipeService's findById method to return the initial recipe
		when(recipeService.findById(1L)).thenReturn(Optional.of(recipe));

		// Call the updateRecipe method
		String result = recipeController.updateRecipe(1L, updatedRecipe);

		// Verify that the method returned the expected string
		assertEquals("Recipe Updated", result);
	}

	@Test
	public void testUpdateRecipeNotFound() {

		// Mock the recipeService's findById method to return an empty optional
		when(recipeService.findById(1L)).thenReturn(Optional.empty());

		// Call the updateRecipe method and verify that it throws a
		// ResourceNotFoundException
		assertThrows(ResourceNotFoundException.class, () -> {
			recipeController.updateRecipe(1L, recipe);
		});

		// Verify that the recipeService's addRecipe method was never called
		verify(recipeService, never()).addRecipe(recipe);
	}

	@Test
	public void testDeleteRecipe() {

		// Mock the recipeService's findById method to return the initial recipe
		when(recipeService.findById(1L)).thenReturn(Optional.of(recipe));

		// Call the deleteRecipes method
		String result = recipeController.deleteRecipes(1L);

		// Verify that the method returned the expected string
		assertEquals("recipe deleted", result);
	}

	@Test
	public void testCreateRecipes() {
		recipeController.createRecipes(recipe);
		verify(recipeService).addRecipe(recipe);
	}

	@Test
	public void testGetRecipesByServingSizeAndIngredients() {
		List<Recipe> recipeList = new ArrayList<>();
		Recipe recipe1 = new Recipe(1L, "Chicken Curry", "Cook the chicken and add curry powder", 6,
				"chicken, curry powder", "true");
		Recipe recipe2 = new Recipe(2L, "Chicken Curry", "Cook the chicken and add curry powder", 6,
				"chicken, curry powder", "true");

		recipeList.add(recipe1);
		recipeList.add(recipe2);

		when(recipeService.findByServingSizeAndIngredients(6, "chicken")).thenReturn(Optional.of(recipeList));

		Optional<List<Recipe>> actualRecipeList = recipeController.getRecipesByServingSizeAndIngredients(6, "chicken");

		assertEquals(recipeList, actualRecipeList.get());
	}

	@Test
	public void testGetRecipesByInstructionsAndIngredients() {
		List<Recipe> recipeList = new ArrayList<>();
		Recipe recipe1 = new Recipe(1L, "Chicken Curry", "Cook the chicken and add curry powder", 6,
				"chicken, curry powder", "true");
		Recipe recipe2 = new Recipe(2L, "Chicken Curry", "Cook the chicken and add curry powder,make it less spicy", 6,
				"chicken, curry powder", "true");

		recipeList.add(recipe1);
		recipeList.add(recipe2);

		when(recipeService.findByInstructionsAndIngredients("Cook the chicken and add curry powder", "chicken"))
				.thenReturn(Optional.of(recipeList));

		Optional<List<Recipe>> actualRecipeList = recipeController
				.getRecipesByServingSizeAndIngredients("Cook the chicken and add curry powder", "chicken");

		assertEquals(recipeList, actualRecipeList.get());
	}
}