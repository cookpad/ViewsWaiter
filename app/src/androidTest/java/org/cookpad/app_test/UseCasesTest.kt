package org.cookpad.rxbroadcaster_app_test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.google.common.truth.Truth.assertThat
import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule
import org.cookpad.app_test.R
import org.cookpad.app_test.home.RecipeHostActivity
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UseCasesTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(RecipeHostActivity::class.java)

    companion object {
        @ClassRule
        @JvmField
        val animRule = DeviceAnimationTestRule()
    }

    @Test
    fun verifyRecipePipeline() {
        clickOnList(position = 0)

        checkLikeStateDetail(liked = false)
        checkBookmarkStateDetail(bookmarked = false)
        checkLikeStateOnList(position = 0, liked = false)
        checkBookmarkStateOnList(position = 0, bookmarked = false)

        clickOnList(position = 0)

        bookmarkRecipeDetail()
        likeRecipeDetail()

        onView(isRoot()).perform(ViewActions.pressBack())

        checkLikeStateDetail(liked = true)
        checkBookmarkStateDetail(bookmarked = true)
        checkLikeStateOnList(position = 0, liked = true)
        checkBookmarkStateOnList(position = 0, bookmarked = true)

        onView(isRoot()).perform(ViewActions.pressBack())

        checkLikeStateOnList(position = 0, liked = true)
        checkBookmarkStateOnList(position = 0, bookmarked = true)
    }

    private fun clickOnList(position: Int) {
        onView(withId(R.id.recyclerViewRecipes))
                .perform(actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(position, click()))
    }

    private fun bookmarkRecipeDetail() {
        onView(withId(R.id.ivDetailBookmarkButton)).perform(click());
    }

    private fun checkBookmarkStateDetail(bookmarked: Boolean) {
        onView(withId(R.id.ivDetailBookmarkButton)).perform(object : ViewAction {
            override fun getConstraints() = isEnabled()
            override fun getDescription() = "Checking like state"

            override fun perform(uiController: UiController, view: View) {
                view.apply {
                    if (bookmarked) {
                        assertThat(tag).isEqualTo("bookmarked")
                    } else {
                        assertThat(tag).isNotEqualTo("bookmarked")
                    }
                }
            }
        })
    }

    private fun likeRecipeDetail() {
        onView(withId(R.id.ivDetailLikeButton)).perform(click());
    }

    private fun checkLikeStateDetail(liked: Boolean) {
        onView(withId(R.id.ivDetailLikeButton)).perform(object : ViewAction {
            override fun getConstraints() = isEnabled()
            override fun getDescription() = "Checking like state"

            override fun perform(uiController: UiController, view: View) {
                view.apply {
                    if (liked) {
                        assertThat(tag).isEqualTo("liked")
                    } else {
                        assertThat(tag).isNotEqualTo("liked")
                    }
                }
            }
        })
    }

    private fun checkLikeStateOnList(position: Int, liked: Boolean) {
        onView(withId(R.id.recyclerViewRecipes))
                .perform(actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(position, object : ViewAction {
                    override fun getConstraints() = isEnabled()
                    override fun getDescription() = "Checking like state"

                    override fun perform(uiController: UiController, view: View) {
                        view.findViewById<ImageView>(R.id.ivLikeButton).apply {
                            if (liked) {
                                assertThat(tag).isEqualTo("liked")
                            } else {
                                assertThat(tag).isNotEqualTo("liked")
                            }
                        }
                    }
                }))
    }

    private fun checkBookmarkStateOnList(position: Int, bookmarked: Boolean) {
        onView(withId(R.id.recyclerViewRecipes))
                .perform(actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(position, object : ViewAction {
                    override fun getConstraints() = isEnabled()
                    override fun getDescription() = "Checking bookmark state"

                    override fun perform(uiController: UiController, view: View) {
                        view.findViewById<ImageView>(R.id.ivBookmarkButton).apply {
                            if (bookmarked) {
                                assertThat(tag).isEqualTo("bookmarked")
                            } else {
                                assertThat(tag).isNotEqualTo("bookmarked")
                            }
                        }
                    }
                }))
    }
}