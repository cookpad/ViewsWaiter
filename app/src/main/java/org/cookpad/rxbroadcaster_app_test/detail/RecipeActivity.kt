package org.cookpad.rxbroadcaster_app_test.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_recipe.*
import org.cookpad.rxbroadcaster_app_test.R
import org.cookpad.rxbroadcaster_app_test.data.models.Recipe
import org.cookpad.rxbroadcaster_app_test.home.recipes.RecipesFragment

class RecipeActivity : AppCompatActivity(), RecipePresenter.View {
    override val recipeId by lazy { intent.extras.getString(keyRecipeId) }

    val onRecipeLikedSubject = PublishSubject.create<Unit>()
    override val onRecipeLiked = onRecipeLikedSubject.hide()

    val onRecipeBookmarkedSubject = PublishSubject.create<Unit>()
    override val onRecipeBookmarked = onRecipeBookmarkedSubject.hide()

    val onRecipeUpdatedFromListSubject = PublishSubject.create<Recipe>()
    override val onRecipeUpdatedFromList = onRecipeUpdatedFromListSubject.hide()

    override val onRecipeUpdatedFromDetailSubject = PublishSubject.create<Recipe>()
    val onRecipeUpdatedFromDetail = onRecipeUpdatedFromDetailSubject.hide()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        lifecycle.addObserver(RecipePresenter(this))

        (fragmentRecipes as? RecipesFragment)?.apply {
            this.onRecipeUpdatedSubject = onRecipeUpdatedFromListSubject
            this.onRecipeUpdated = onRecipeUpdatedFromDetail
        }

        ivLikeButton.setOnClickListener { onRecipeLikedSubject.onNext(Unit) }
        ivBookmarkButton.setOnClickListener { onRecipeBookmarkedSubject.onNext(Unit) }
    }

    override fun showRecipe(recipe: Recipe) {
        recipe.apply {
            tvTitle.text = name
            tvDescription.text = description
            ivRecipeImage.setImageResource(imageRes)
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

    override fun setLiked(liked: Boolean) {
        ivLikeButton.apply {
            val (drawable, colorFilter) = if (liked) {
                R.drawable.ic_liked to ContextCompat.getColor(context, R.color.likedColor)
            } else {
                R.drawable.ic_like to ContextCompat.getColor(context, R.color.textColor)
            }

            setImageResource(drawable)
            setColorFilter(colorFilter)
        }
    }

    override fun setBookmarked(bookmarked: Boolean) {
        ivBookmarkButton.apply {
            val drawableBookmark = if (bookmarked) R.drawable.ic_bookmarked else R.drawable.ic_bookmark
            setImageResource(drawableBookmark)
        }
    }
}