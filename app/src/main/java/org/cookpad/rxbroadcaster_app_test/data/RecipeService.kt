package org.cookpad.rxbroadcaster_app_test.data

import org.cookpad.rxbroadcaster_app_test.data.models.Recipe

object RecipeService {
    val recipes = mutableListOf(Recipe("123", "Chicken", "A very good chicken", false, false))

    fun getAll() = recipes.toList()

    fun updateRecipe(recipe: Recipe) {
        val index = recipes.indexOfFirst { it.id == recipe.id }
        recipes[index] = recipe
    }
}