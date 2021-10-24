package co.feliperivera.mooveitworkshop.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import co.feliperivera.mooveitworkshop.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun showMovieTitle() {
        onView(withId(R.id.movies)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnFirstMovieAndOpenDetailFragment() {
        onView(withId(R.id.list))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<MovieRecyclerViewAdapter.ViewHolder>(0,click()))
        onView(withId(R.id.poster)).check(matches(isDisplayed()))
        onView(withId(R.id.overview)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_title)).check(matches(isDisplayed()))
        onView(withId(R.id.ratingBar)).check(matches(isDisplayed()))
        onView(withId(R.id.genres)).check(matches(isDisplayed()))
        onView(withId(R.id.reviews_button)).check(matches(isDisplayed()))
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnBackButtonOnDetailFragment() {
        onView(withId(R.id.list))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<MovieRecyclerViewAdapter.ViewHolder>(0,click()))
        onView(withId(R.id.back_button)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.movies)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnReviewsButtonAndOpenReviewsFragment() {
        onView(withId(R.id.list))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<MovieRecyclerViewAdapter.ViewHolder>(0,click()))
        onView(withId(R.id.reviews_button)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.list_reviews)).check(matches(isDisplayed()))
        onView(withId(R.id.poster)).check(matches(isDisplayed()))
        onView(withId(R.id.review_title)).check(matches(isDisplayed()))
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
    }


    @Test
    fun clickOnBackButtonOnReviewFragment() {
        onView(withId(R.id.list))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<MovieRecyclerViewAdapter.ViewHolder>(0,click()))
        onView(withId(R.id.reviews_button)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.back_button)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.movie_title)).check(matches(isDisplayed()))
    }
}