package org.cookpad.rxbroadcaster_app_test.data

import org.cookpad.rxbroadcaster_app_test.data.models.Recipe

private val recipes = mutableListOf(Recipe("123", "Chicken", "A very good chicken", false, false))

class RecipeRepository {

    fun getAll() = recipes.toList()

    fun get(id: String) = recipes.first { it.id == id }

    fun updateRecipe(recipe: Recipe) {
        val index = recipes.indexOfFirst { it.id == recipe.id }
        recipes[index] = recipe
    }
}