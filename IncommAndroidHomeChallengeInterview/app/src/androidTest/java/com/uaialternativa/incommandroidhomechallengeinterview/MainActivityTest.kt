package com.uaialternativa.incommandroidhomechallengeinterview

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * INTEGRATION / INSTRUMENTED TEST
 *
 * This class runs on an Android device/emulator.
 * 
 * Goal: Verify if the MainActivity launches and displays the RecyclerView.
 * This is an integration test because it involves the Activity, Layout interactions,
 * and indirectly the whole stack initialization.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @Test
    fun activityShouldLaunchAndDisplayRecyclerView() {
        // Launch Activity
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Check if RecyclerView is displayed
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        scenario.close()
    }
}
