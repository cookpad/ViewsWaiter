package org.cookpad.app_test.data.models

data class Recipe(
        val id: String,
        val name: String,
        val description: String,
        val liked: Boolean,
        val bookmarked: Boolean,
        val imageRes: Int
)