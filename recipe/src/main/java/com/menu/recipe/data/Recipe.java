package com.menu.recipe.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipe")

public class Recipe {

	public Recipe() {
		// default constructor
	}

	public Recipe(Long id, String name, String instructions, int servingSize, String vegetarian, String ingredients) {
		super();
		this.id = id;
		this.name = name;
		this.instructions = instructions;
		this.servingSize = servingSize;
		this.vegetarian = vegetarian;
		this.ingredients = ingredients;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "instructions")
	private String instructions;
	@Column(name = "servingSize")
	private int servingSize;
	@Column(name = "vegetarian")
	private String vegetarian;
	@Column(name = "ingredients")
	private String ingredients;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public int getServingSize() {
		return servingSize;
	}

	public void setServingSize(int servingSize) {
		this.servingSize = servingSize;
	}

	public String getVegetarian() {
		return vegetarian;
	}

	public void setVegetarian(String vegetarian) {
		this.vegetarian = vegetarian;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	@Override
	public String toString() {
		return "Recipe [id=" + id + ", name=" + name + ", instructions=" + instructions + ", servingSize=" + servingSize
				+ ", isVegetarian=" + vegetarian + ", ingredients=" + ingredients + "]";
	}

}
