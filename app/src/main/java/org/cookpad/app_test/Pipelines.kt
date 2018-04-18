package org.cookpad.app_test

import org.cookpad.app_test.data.models.Recipe
import org.cookpad.views_waiter.ViewsWaiter

sealed class RecipeAction(val recipe: Recipe)
class RecipeActionLike(recipe: Recipe) : RecipeAction(recipe)
class RecipeActionBookmark(recipe: Recipe) : RecipeAction(recipe)

object Pipelines {
    val recipeActionPipeline = ViewsWaiter<RecipeAction>()
}