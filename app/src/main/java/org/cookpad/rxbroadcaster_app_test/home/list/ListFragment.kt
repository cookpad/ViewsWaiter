package org.cookpad.rxbroadcaster_app_test.home.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list.*
import miguelbcr.ok_adapters.recycler_view.OkRecyclerViewAdapter
import org.cookpad.rxbroadcaster_app_test.R
import org.cookpad.rxbroadcaster_app_test.home.RecipeAdapter
import org.cookpad.rxbroadcaster_app_test.models.Recipe

class ListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false) as ViewGroup
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = object : OkRecyclerViewAdapter<Recipe, RecipeAdapter>() {
            override fun onCreateItemView(parent: ViewGroup, viewType: Int) = RecipeAdapter(parent.context)
        }
        recyclerView.adapter = adapter

        adapter.add(Recipe("123", "Chicken", "A very good chicken", false, false))
    }
}