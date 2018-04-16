package org.cookpad.rxbroadcaster_app_test.home

import android.content.Context
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
        }
    }
}