package com.menu.recipe.service;

import java.util.List;
import java.util.Optional;

import com.menu.recipe.data.Recipe;

public interface RecipeService {
	void addRecipe(Recipe recipe);

	void removeRecipe(long id);

	List<Recipe> getAllRecipes();

	Optional<Recipe> findById(long id);

	Optional<List<Recipe>> searchRecipes(String name, String vegetarian, Integer servingSize, String instructions,
			String ingredients);

	Optional<List<Recipe>> findByServingSizeAndIngredients(int servingSize, String ingredients);

	Optional<List<Recipe>> findByInstructionsAndIngredients(String instructions, String ingredient);
}
