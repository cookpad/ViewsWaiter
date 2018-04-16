package org.cookpad.rxbroadcaster_app_test.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_recipe.*
import org.cookpad.rxbroadcaster_app_test.R
import org.cookpad.rxbroadcaster_app_test.data.models.Recipe

class RecipeActivity : AppCompatActivity(), RecipePresenter.View {
    override val recipeId by lazy { intent.extras.getString(keyRecipeId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        lifecycle.addObserver(RecipePresenter(this))
    }

    override fun showRecipe(recipe: Recipe) {
        recipe.apply {
            tvTitle.text = name
        }
    }

    companion object {
        val keyRecipeId = "keyRecipeId"

        fun startRecipeActivity(context: Context, recipeId: String) {
            val intent = Intent(context, RecipeActivity::class.java).apply {
                putExtra(keyRecipeId, recipeId)
            }

            context.startActivity(intent)
        }
    }
}