package org.cookpad.rxbroadcaster_app_test

import org.cookpad.rxbroadcaster.RxBroadcaster
import org.cookpad.rxbroadcaster_app_test.data.models.Recipe

sealed class RecipeAction(val recipe: Recipe)
class RecipeActionLike(recipe: Recipe) : RecipeAction(recipe)
class RecipeActionBookmark(recipe: Recipe) : RecipeAction(recipe)

object Pipelines {
    val recipeActionPipeline = RxBroadcaster<RecipeAction>()
}