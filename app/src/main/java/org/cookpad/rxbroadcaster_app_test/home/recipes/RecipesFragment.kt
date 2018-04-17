package org.cookpad.rxbroadcaster_app_test.home.recipes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_recipes.*
import org.cookpad.rxbroadcaster_app_test.R
import org.cookpad.rxbroadcaster_app_test.data.models.Recipe
import org.cookpad.rxbroadcaster_app_test.detail.RecipeActivity
import org.cookpad.rxbroadcaster_app_test.home.adapters.RecipeAdapter

class RecipesFragment : Fragment(), RecipesPresenter.View {
    override val detailClicks by lazy { PublishSubject.create<Recipe>() }
    override val bookmarkClicks by lazy { PublishSubject.create<Recipe>() }
    override val likeClicks by lazy { PublishSubject.create<Recipe>() }

    override var onRecipeUpdatedSubject: PublishSubject<Recipe>? = null
    override var onRecipeUpdated: Observable<Recipe>? = null

    private val adapter by lazy { RecipeAdapter(detailClicks, likeClicks, bookmarkClicks) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_recipes, container, false) as ViewGroup

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        lifecycle.addObserver(RecipesPresenter(this))
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun showRecipes(recipes: List<Recipe>) {
        adapter.setAll(recipes)
    }

    override fun goToRecipeScreen(recipeId: String) {
        activity?.let { RecipeActivity.startRecipeActivity(it, recipeId) }
    }
}