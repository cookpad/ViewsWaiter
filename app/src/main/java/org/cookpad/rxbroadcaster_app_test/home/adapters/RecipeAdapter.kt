package org.cookpad.rxbroadcaster_app_test.home.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.list_item_recipe.view.*
import org.cookpad.rxbroadcaster_app_test.R
import org.cookpad.rxbroadcaster_app_test.data.models.Recipe

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
                val drawableBookmark = if (recipe.bookmarked) R.drawable.ic_bookmarked else R.drawable.ic_bookmark
                setImageResource(drawableBookmark)
                setOnClickListener { bookmarkClicks.onNext(recipe) }
            }

            ivLikeButton.apply {
                val (drawable, colorFilter) = if (recipe.liked) {
                    R.drawable.ic_liked to ContextCompat.getColor(context, R.color.likedColor)
                } else {
                    R.drawable.ic_like to ContextCompat.getColor(context, R.color.textColor)
                }

                setImageResource(drawable)
                setColorFilter(colorFilter)
                setOnClickListener { likeClicks.onNext(recipe) }
            }

            rlRoot.setOnClickListener { detailClicks.onNext(recipe) }
        }
    }

    fun setAll(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    override fun getItemCount() = recipes.size
}