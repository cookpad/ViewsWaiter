package org.cookpad.rxbroadcaster_app_test.home.recipes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recipes.*
import miguelbcr.ok_adapters.recycler_view.OkRecyclerViewAdapter
import org.cookpad.rxbroadcaster_app_test.R
import org.cookpad.rxbroadcaster_app_test.data.models.Recipe
import org.cookpad.rxbroadcaster_app_test.detail.RecipeActivity
import org.cookpad.rxbroadcaster_app_test.home.adapters.RecipeAdapter

class RecipesFragment : Fragment(), RecipesPresenter.View {
    private val adapter by lazy {
        object : OkRecyclerViewAdapter<Recipe, RecipeAdapter>() {
            override fun onCreateItemView(parent: ViewGroup, viewType: Int) = RecipeAdapter(parent.context)
        }.apply {
            setOnItemClickListener { item, _, _ -> activity?.let { RecipeActivity.startRecipeActivity(it, item.id) } }
        }
    }

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
        adapter.all = recipes
    }
}