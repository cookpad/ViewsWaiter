package org.cookpad.rxbroadcaster_app_test.models

data class Recipe(
        val id: String,
        val name: String,
        val description: String,
        val liked: Boolean,
        val bookmarked: Boolean
)