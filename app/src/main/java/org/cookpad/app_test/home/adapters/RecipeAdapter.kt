package org.cookpad.app_test.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.list_item_recipe.view.*
import org.cookpad.app_test.R
import org.cookpad.app_test.data.models.Recipe

class RecipeAdapter(val detailClicks: PublishSubject<Recipe>,
                    val likeClicks: PublishSubject<Recipe>,
                    val bookmarkClicks: PublishSubject<Recipe>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var recipes = emptyList<Recipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_recipe, parent, false)
            .run {
                object : RecyclerView.ViewHolder(this) {}
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            val recipe = recipes[position]

            tvTitle.text = recipe.name
            tvDescription.text = recipe.description

            ivBookmarkButton.apply {
                setBookmarked(this, recipe.bookmarked)
                setOnClickListener {
                    bookmarkClicks.onNext(recipe)
                }
            }

            ivLikeButton.apply {
                setLiked(this, recipe.liked)
                setOnClickListener {
                    likeClicks.onNext(recipe)
                }
            }

            rlRoot.setOnClickListener { detailClicks.onNext(recipe) }

            ivRecipeImage.setImageResource(recipe.imageRes)
        }
    }

    private fun setBookmarked(ivBookmarkButton: ImageView, bookmarked: Boolean) {
        ivBookmarkButton.apply {
            val drawableBookmark = if (bookmarked) R.drawable.ic_bookmarked else R.drawable.ic_bookmark
            setImageResource(drawableBookmark)
            tag = if (bookmarked) "bookmarked" else ""
        }
    }

    private fun setLiked(ivLikeButton: ImageView, liked: Boolean) {
        ivLikeButton.apply {
            val (drawable, colorFilter, tag) = if (liked) {
                Triple(R.drawable.ic_liked, ContextCompat.getColor(context, R.color.likedColor), "liked")
            } else {
                Triple(R.drawable.ic_like, ContextCompat.getColor(context, R.color.textColor), "")
            }

            setImageResource(drawable)
            setColorFilter(colorFilter)
            setTag(tag)
        }
    }

    fun setAll(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    override fun getItemCount() = recipes.size
}