package org.cookpad.rxbroadcaster_app_test.detail

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
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

            onRecipeLiked
                    .flatMapSingle { repository.get(recipeId) }
                    .flatMapCompletable { recipe ->
                        val updatedRecipe = recipe.copy(liked = !recipe.liked)
                        repository.updateRecipe(updatedRecipe).doOnComplete {
                            setLiked(updatedRecipe.liked)
                            onRecipeUpdatedFromDetailSubject.onNext(updatedRecipe)
                        }
                    }
                    .subscribe()
                    .addTo(disposables)

            onRecipeBookmarked
                    .flatMapSingle { repository.get(recipeId) }
                    .flatMapCompletable { recipe ->
                        val updatedRecipe = recipe.copy(bookmarked = !recipe.bookmarked)
                        repository.updateRecipe(updatedRecipe).doOnComplete {
                            setBookmarked(updatedRecipe.bookmarked)
                            onRecipeUpdatedFromDetailSubject.onNext(updatedRecipe)
                        }
                    }
                    .subscribe()
                    .addTo(disposables)

            onRecipeUpdatedFromList
                    .flatMapSingle { repository.get(recipeId) }
                    .subscribe { recipe ->
                        setBookmarked(recipe.bookmarked)
                        setLiked(recipe.liked)
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

        val recipeId: String
        fun showRecipe(recipe: Recipe)
        fun setLiked(liked: Boolean)
        fun setBookmarked(bookmarked: Boolean)
    }
}
