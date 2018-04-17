package org.cookpad.rxbroadcaster_app_test.detail

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.cookpad.rxbroadcaster_app_test.Pipelines
import org.cookpad.rxbroadcaster_app_test.RecipeAction
import org.cookpad.rxbroadcaster_app_test.RecipeActionBookmark
import org.cookpad.rxbroadcaster_app_test.RecipeActionLike
import org.cookpad.rxbroadcaster_app_test.data.RecipeRepository
import org.cookpad.rxbroadcaster_app_test.data.models.Recipe
import org.cookpad.rxbroadcaster_app_test.utils.extensions.addTo

class RecipePresenter(private val view: View,
                      private val repository: RecipeRepository = RecipeRepository()) : LifecycleObserver {
    private val disposables = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        view.apply {
            repository.get(recipeId)
                    .subscribe { recipe ->
                        showRecipe(recipe)
                        setBookmarked(recipe.bookmarked)
                        setLiked(recipe.liked)
                    }
                    .addTo(disposables)

            fun updateRecipe(recipeAction: RecipeAction) = repository.updateRecipe(recipeAction.recipe).doOnComplete {
                // Notify the BookmarksFragment of the new bookmarked/unbookmarked recipe
                onRecipeUpdatedFromDetailSubject.onNext(recipeAction.recipe)
                Pipelines.recipeActionPipeline.channel(recipeAction.recipe.id).emit(recipeAction)
                setLiked(recipeAction.recipe.liked)
                setBookmarked(recipeAction.recipe.bookmarked)
            }

            onRecipeLiked
                    .flatMapSingle { repository.get(recipeId) }
                    .flatMapCompletable { recipe -> updateRecipe(RecipeActionLike(recipe.copy(liked = !recipe.liked))) }
                    .subscribe()
                    .addTo(disposables)

            onRecipeBookmarked
                    .flatMapSingle { repository.get(recipeId) }
                    .flatMapCompletable { recipe -> updateRecipe(RecipeActionBookmark(recipe.copy(bookmarked = !recipe.bookmarked))) }
                    .subscribe()
                    .addTo(disposables)

            onRecipeUpdatedFromList
                    .filter { it.id == recipeId }
                    .flatMapSingle { repository.get(recipeId) }
                    .subscribe { recipe ->
                        setBookmarked(recipe.bookmarked)
                        setLiked(recipe.liked)
                    }
                    .addTo(disposables)

            onRecipeActionPipeline
                    .subscribe { recipeAction ->
                        recipeAction.apply {
                            when (this) {
                                is RecipeActionLike -> setLiked(recipe.liked)
                                is RecipeActionBookmark -> setBookmarked(recipe.bookmarked)
                            }
                        }
                        println("detail")
                    }
                    .addTo(disposables)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposables.dispose()
    }

    interface View {
        val onRecipeLiked: Observable<Unit>
        val onRecipeBookmarked: Observable<Unit>
        val onRecipeUpdatedFromList: Observable<Recipe>
        val onRecipeUpdatedFromDetailSubject: PublishSubject<Recipe>

        val onRecipeActionPipeline: Observable<RecipeAction>

        val recipeId: String
        fun showRecipe(recipe: Recipe)
        fun setLiked(liked: Boolean)
        fun setBookmarked(bookmarked: Boolean)
    }
}
