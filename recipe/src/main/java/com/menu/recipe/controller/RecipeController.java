package com.menu.recipe.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.menu.recipe.exception.*;
import com.menu.recipe.data.Recipe;
import com.menu.recipe.service.*;
import io.swagger.v3.oas.annotations.Operation;

import com.menu.recipe.repository.*;

@RestController

public class RecipeController {

	@Autowired
	RecipeRepository recipeRepository;
	@Autowired
	RecipeService recipeService;

	@GetMapping("/recipes")
	@Operation(summary = "get all the recipes")

	public List<Recipe> getAllRecipes() {
		return recipeService.getAllRecipes();
	}

	@PostMapping("/recipes")
	@Operation(summary = "add a new recipe")

	public String createRecipes(@RequestBody Recipe recipe) {
		recipeService.addRecipe(recipe);
		return "New Recipe Created";
	}

	@PutMapping("/recipes/{id}")
	@Operation(summary = "update an existing recipe by id")

	public String updateRecipe(@PathVariable("id") long id, @RequestBody Recipe recipe) {
		return recipeService.findById(id).map(p -> {
			p.setName(recipe.getName());
			p.setInstructions(recipe.getInstructions());
			p.setIngredients(recipe.getIngredients());
			p.setServingSize(recipe.getServingSize());
			p.setVegetarian(recipe.getVegetarian());
			recipeService.addRecipe(p);
			return "Recipe Updated";
		}).orElseThrow(() -> new ResourceNotFoundException("recipe " + id + " not found"));
	}

	@DeleteMapping("/recipes/{id}")
	@Operation(summary = "delete an existing recipe by id")

	public String deleteRecipes(@PathVariable("id") long id) {
		return recipeService.findById(id).map(p -> {
			recipeService.removeRecipe(id);
			return "recipe deleted";
		}).orElseThrow(() -> new ResourceNotFoundException("recipe " + id + " not found"));
	}

	@GetMapping("/recipes/search")
	@Operation(summary = "get recipes filter by name,vegetarian or not,serving size,instructions and ingredients")

	public Optional<List<Recipe>> getRecipes(@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "vegetarian", required = false) String vegetarian,
			@RequestParam(name = "servingSize", required = false ,defaultValue ="0") Integer servingSize,
			@RequestParam(name = "instructions", required = false) String instructions,
			@RequestParam(name = "ingredients", required = false) String ingredients) {
		return recipeService.searchRecipes(name, vegetarian, servingSize, instructions, ingredients);
	}

	@GetMapping("/recipes/getRecipesByServingSizeAndIngredients")
	@Operation(summary = "get recipes by serving size and ingredients")

	public Optional<List<Recipe>> getRecipesByServingSizeAndIngredients(
			@RequestParam(value = "servingSize", required = true) Integer servingSize,
			@RequestParam(value = "ingredients", required = true) String ingredients) {
		return recipeService.findByServingSizeAndIngredients(servingSize, ingredients);

	}

	@GetMapping("/recipes/getRecipesByInstructionsAndIngredients")
	@Operation(summary = "get recipes by isntructions and ingredients")

	public Optional<List<Recipe>> getRecipesByServingSizeAndIngredients(
			@RequestParam(value = "isntructions", required = true) String isntructions,
			@RequestParam(value = "ingredients", required = true) String ingredients) {
		return recipeService.findByInstructionsAndIngredients(isntructions, ingredients);

	}

}
