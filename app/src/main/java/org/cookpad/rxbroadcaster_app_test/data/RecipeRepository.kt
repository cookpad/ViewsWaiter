package org.cookpad.rxbroadcaster_app_test.data

import org.cookpad.rxbroadcaster_app_test.data.models.Recipe

private val recipes = mutableListOf(
        Recipe("1", "Chicken", "A very good chicken", false, false),
        Recipe("2", "Salad", "A very good salad", false, true)
)

class RecipeRepository {

    fun getAll() = recipes.toList()

    fun getBookmarks() = recipes.filter { it.bookmarked }

    fun get(id: String) = recipes.first { it.id == id }

    fun updateRecipe(recipe: Recipe) {
        val index = recipes.indexOfFirst { it.id == recipe.id }
        recipes[index] = recipe
    }
}