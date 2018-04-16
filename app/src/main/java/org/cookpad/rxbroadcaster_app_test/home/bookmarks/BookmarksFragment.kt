package org.cookpad.rxbroadcaster_app_test.home.bookmarks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recipes.*
import org.cookpad.rxbroadcaster_app_test.R
import org.cookpad.rxbroadcaster_app_test.data.models.Recipe
import org.cookpad.rxbroadcaster_app_test.detail.RecipeActivity
import org.cookpad.rxbroadcaster_app_test.home.adapters.RecipeAdapter

class BookmarksFragment : Fragment(), BookmarksPresenter.View {
    private val adapter by lazy {
        val detailClicks: (Recipe) -> Unit = { recipe -> activity?.let { RecipeActivity.startRecipeActivity(it, recipe.id) } }
        RecipeAdapter(detailClicks)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_bookmarks, container, false) as ViewGroup

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        lifecycle.addObserver(BookmarksPresenter(this))
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun showBookmarks(recipes: List<Recipe>) {
        adapter.setAll(recipes)
    }
}