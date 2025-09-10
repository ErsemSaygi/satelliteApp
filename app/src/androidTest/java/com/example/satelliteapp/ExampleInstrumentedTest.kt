package com.example.satelliteapp

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.satelliteapp.presentation.list.ListFragment
import com.example.satelliteapp.presentation.list.ListFragmentDirections
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import launchFragmentInHiltContainer
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@HiltAndroidTest
class ListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun recyclerViewIsVisible() {
        launchFragmentInHiltContainer<ListFragment> {
            onView(withId(R.id.satellite_list))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun searchBarFiltersList() {
        launchFragmentInHiltContainer<ListFragment> {
            // SearchBar'a "test" yaz
            onView(withId(R.id.search_bar))
                .perform(typeText("test"), pressImeActionButton())

            onView(withId(R.id.satellite_list))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun clickItemNavigatesToDetail() {
        launchFragmentInHiltContainer<ListFragment> {
            val navController = mock(NavController::class.java)
            Navigation.setViewNavController(requireView(), navController)

            onView(withId(R.id.satellite_list))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0, click()
                    )
                )

            verify(navController).navigate(
                ListFragmentDirections.actionListFragmentToDetailFragment(
                    1, // burada gerçek id'yi fake data ile eşleştirmen gerekebilir
                    "name"
                )
            )
        }
    }
}
