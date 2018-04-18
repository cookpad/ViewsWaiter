package org.cookpad.app_test.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_recipe_list.*
import org.cookpad.app_test.R
import org.cookpad.app_test.data.models.Recipe
import org.cookpad.app_test.home.bookmarks.BookmarksFragment
import org.cookpad.app_test.home.recipes.RecipesFragment

class RecipeHostActivity : AppCompatActivity(), RecipeHostPresenter.View {
    val onShowListSubject = PublishSubject.create<Unit>()
    override val onShowList = onShowListSubject.hide()

    val onShowBookmarksSubject = PublishSubject.create<Unit>()
    override val onShowBookmarks = onShowBookmarksSubject.hide()

    val onRecipeUpdatedFromListSubject = PublishSubject.create<Recipe>()
    val onRecipeUpdatedFromList = onRecipeUpdatedFromListSubject.hide()

    val onRecipeUpdatedFromBookmarksSubject = PublishSubject.create<Recipe>()
    val onRecipeUpdatedFromBookmarks = onRecipeUpdatedFromBookmarksSubject.hide()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        lifecycle.addObserver(RecipeHostPresenter(this))
    }

    override fun setupNavigation() {
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> RecipesFragment().apply {
                        this.onRecipeUpdated = this@RecipeHostActivity.onRecipeUpdatedFromBookmarks
                        this.onRecipeUpdatedSubject = this@RecipeHostActivity.onRecipeUpdatedFromListSubject
                    }
                    1 -> BookmarksFragment().apply {
                        this.onRecipeUpdated = this@RecipeHostActivity.onRecipeUpdatedFromList
                        this.onRecipeUpdatedSubject = this@RecipeHostActivity.onRecipeUpdatedFromBookmarksSubject
                    }
                    else -> BookmarksFragment().apply {
                        this.onRecipeUpdated = this@RecipeHostActivity.onRecipeUpdatedFromList
                        this.onRecipeUpdatedSubject = this@RecipeHostActivity.onRecipeUpdatedFromBookmarksSubject
                    }
                }
            }

            override fun getCount() = 2
        }

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_list -> onShowListSubject.onNext(Unit)
                R.id.menu_bookmarks -> onShowBookmarksSubject.onNext(Unit)
            }
            true
        }
    }

    override fun showList() {
        viewPager.currentItem = 0
    }

    override fun showBookmarks() {
        viewPager.currentItem = 1
    }
}
