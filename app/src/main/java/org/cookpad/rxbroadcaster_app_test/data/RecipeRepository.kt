package org.cookpad.rxbroadcaster_app_test.data

import io.reactivex.Completable
import io.reactivex.Single
import org.cookpad.rxbroadcaster_app_test.R
import org.cookpad.rxbroadcaster_app_test.data.models.Recipe

private val recipes = mutableListOf(
        Recipe("1", "Chicken",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ultrices felis et dolor maximus lobortis. Curabitur et semper enim, vitae molestie purus. Etiam sed euismod massa. Donec a placerat dui, pretium posuere felis. Phasellus volutpat libero mattis neque ultrices, a molestie lectus tempor. Duis interdum nunc quis nisi volutpat, eget egestas mauris congue. Nunc pulvinar id orci at scelerisque.",
                false, false, R.drawable.chicken),
        Recipe("2", "Salad",
                "Praesent mattis ligula lectus. Morbi tincidunt lorem vitae ipsum sagittis convallis. Nam at sapien at nibh posuere mollis ut et tortor. Cras vel tellus in massa malesuada accumsan vitae sed ex. Maecenas lobortis, sapien a eleifend fringilla, turpis mi dignissim mauris, eget aliquam ex nulla vel magna. Pellentesque in ipsum sed leo blandit egestas. Nullam eu arcu ut eros blandit interdum. Aliquam erat volutpat. In blandit fringilla dolor eu tempus.",
                false, false, R.drawable.salad),
        Recipe("3", "Pasta",
                "Curabitur non eleifend tellus, at luctus sapien. Phasellus ut sodales dolor. Praesent consectetur elit quis odio egestas consectetur. Morbi venenatis ac justo a ullamcorper. Phasellus turpis lacus, scelerisque ut lectus eget, bibendum sagittis felis. Suspendisse potenti. Sed arcu odio, sollicitudin interdum sodales eu, eleifend non velit. Vivamus placerat dolor sit amet nisl aliquet tempus. Pellentesque sed pulvinar ante. Morbi risus odio, viverra non arcu sit amet, eleifend commodo massa.",
                false, false, R.drawable.pasta)
)

class RecipeRepository {

    fun getAll() = recipes.toList().let { Single.just(it) }

    fun getBookmarks() = recipes.filter { it.bookmarked }.let { Single.just(it) }

    fun get(id: String) = recipes.first { it.id == id }.let { Single.just(it) }

    fun updateRecipe(recipe: Recipe) = Completable.fromAction {
        val index = recipes.indexOfFirst { it.id == recipe.id }
        recipes[index] = recipe
    }
}