package org.cookpad.app_test.home.bookmarks

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_bookmarks.*
import org.cookpad.app_test.Pipelines
import org.cookpad.app_test.R
import org.cookpad.app_test.data.models.Recipe
import org.cookpad.app_test.detail.RecipeActivity
import org.cookpad.app_test.home.adapters.RecipeAdapter
import org.cookpad.views_waiter.bindOnBackground

class BookmarksFragment : androidx.fragment.app.Fragment(), BookmarksPresenter.View {
    override val detailClicks by lazy { PublishSubject.create<Recipe>() }
    override val bookmarkClicks by lazy { PublishSubject.create<Recipe>() }
    override val likeClicks by lazy { PublishSubject.create<Recipe>() }

    private val adapter by lazy { RecipeAdapter(detailClicks, likeClicks, bookmarkClicks) }

    override var onRecipeUpdatedSubject: PublishSubject<Recipe>? = null
    override var onRecipeUpdated: Observable<Recipe>? = null

    override val onRecipeActionPipeline by lazy {
        Pipelines.recipeActionPipeline.stream().bindOnBackground(lifecycle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_bookmarks, container, false) as ViewGroup

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        lifecycle.addObserver(BookmarksPresenter(this))
    }

    private fun setupRecyclerView() {
        recyclerViewBookmarks.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerViewBookmarks.adapter = adapter
    }

    override fun showBookmarks(recipes: List<Recipe>) {
        adapter.setAll(recipes)
    }

    override fun goToRecipeScreen(recipeId: String) {
        activity?.let { RecipeActivity.startRecipeActivity(it, recipeId) }
    }
}