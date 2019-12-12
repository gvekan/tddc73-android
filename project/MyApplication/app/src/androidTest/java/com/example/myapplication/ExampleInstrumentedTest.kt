package com.example.myapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(
        MainActivity::class.java
    )

    @Before
    fun setup() { // Setting up before every test
    }

    @Test
    fun testSwipeLeft() {
        onView(allOf(withParentIndex(1), withClassName(endsWith("RadioButton")))).check(matches(not(isChecked())))
        onView(withId(R.id.carouselView)).perform(swipeLeft())
        onView(allOf(withParentIndex(1), withClassName(endsWith("RadioButton")))).check(matches(isChecked()))
    }

    @Test
    fun testRadioButton() {
        onView(withText("Do you like this app?")).check(doesNotExist())
        onView(allOf(withParentIndex(2), withClassName(endsWith("RadioButton")))).perform(click())
        onView(withText("Do you like this app?")).check(matches(isDisplayed()))
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.myapplication", appContext.packageName)
    }
}
