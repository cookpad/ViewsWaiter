package org.cookpad.rxbroadcaster_app_test.home

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.widget.RelativeLayout
import miguelbcr.ok_adapters.recycler_view.OkRecyclerViewAdapter
import org.cookpad.rxbroadcaster_app_test.models.Recipe
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.list_item_recipe.view.*
import org.cookpad.rxbroadcaster_app_test.R


class RecipeAdapter(context: Context) : RelativeLayout(context), OkRecyclerViewAdapter.Binder<Recipe> {
    val view: View = LayoutInflater.from(getContext()).inflate(R.layout.list_item_recipe, this, true)

    override fun bind(recipe: Recipe, position: Int, count: Int) {
        view.apply {
            title.text = recipe.name
            description.text = recipe.description

            setLiked(recipe.liked)
            setBookmarked(recipe.bookmarked)
        }
    }

    fun setBookmarked(bookmarked: Boolean) {
        if (bookmarked) {
            ivBookmarkButton.setImageResource(R.drawable.ic_bookmarked)
        } else {
            ivBookmarkButton.setImageResource(R.drawable.ic_bookmark)
        }
    }

    fun setLiked(liked: Boolean) {
        if (liked) {
            ivLikeButton.setImageResource(R.drawable.ic_liked)
            ivLikeButton.setColorFilter(ContextCompat.getColor(context, R.color.likedColor))
        } else {
            ivLikeButton.setImageResource(R.drawable.ic_like)
            ivLikeButton.setColorFilter(ContextCompat.getColor(context, R.color.textColor))
        }
    }
}