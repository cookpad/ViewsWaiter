package org.cookpad.rxbroadcaster_app_test.home.recipes

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.cookpad.rxbroadcaster_app_test.data.RecipeRepository
import org.cookpad.rxbroadcaster_app_test.data.models.Recipe
import org.cookpad.rxbroadcaster_app_test.utils.extensions.addTo

class RecipesPresenter(private val view: View,
                       private val repository: RecipeRepository = RecipeRepository()) : LifecycleObserver {
    private val disposables = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        view.apply {
            detailClicks
                    .subscribe { recipe -> goToRecipeScreen(recipe.id) }
                    .addTo(disposables)

            likeClicks
                    .flatMapCompletable { recipe -> repository.toggleLike(recipe).doOnComplete { showRecipes() } }
                    .subscribe()
                    .addTo(disposables)

            bookmarkClicks
                    .flatMapCompletable { recipe -> repository.toggleBookmark(recipe).doOnComplete { showRecipes() } }
                    .subscribe()
                    .addTo(disposables)
        }

        showRecipes()
    }

    private fun showRecipes() {
        view.apply {
            repository.getAll()
                    .subscribe { recipes -> showRecipes(recipes) }
                    .addTo(disposables)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposables.dispose()
    }

    interface View {
        val detailClicks: PublishSubject<Recipe>
        val likeClicks: PublishSubject<Recipe>
        val bookmarkClicks: PublishSubject<Recipe>

        fun showRecipes(recipes: List<Recipe>)
        fun goToRecipeScreen(recipeId: String)
    }
}