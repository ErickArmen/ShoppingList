package com.eoma.shoppinglist.presentation.activities.main

import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeRight
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.intent.Intents.*
import android.support.test.espresso.intent.matcher.IntentMatchers.*
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.eoma.shoppinglist.R
import com.eoma.shoppinglist.presentation.activities.list.ActivityList
import com.eoma.shoppinglist.presentation.recycler.RecyclerAdapter2
import com.eoma.shoppinglist.presentation.recycler.RecyclerViewHolder2
import com.eoma.shoppinglist.sqlite.EntityListNames
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @get:Rule
    val activityRule = IntentsTestRule<MainActivity>(MainActivity::class.java, true, true)
    private lateinit var adapter: RecyclerAdapter2<EntityListNames>
    private val listTest = "Test"

    @Before
    fun setUp(){

        adapter = activityRule.activity.findViewById<RecyclerView>(R.id.recyclerView).adapter as RecyclerAdapter2<EntityListNames>
        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.etDialog)).perform(typeText(listTest), closeSoftKeyboard())
        onView(withId(android.R.id.button1)).perform(click())
        pressBack()
    }

    @After
    fun removeItemAfter(){
        val countbefore = adapter.itemCount
        onView(withId(R.id.recyclerView))
                .perform(actionOnItemAtPosition<RecyclerViewHolder2>(countbefore-1, swipeRight()))
        onView(withId(android.R.id.button1)).perform(click())
        assertEquals(countbefore-1, adapter.itemCount)
    }

    @Test
    fun checkItemCreated(){
        assertEquals(listTest, adapter.getItem(adapter.itemCount -1).listname)
    }

    @Test
    fun checkList(){

        onView(withId(R.id.recyclerView)).perform(actionOnItemAtPosition<RecyclerViewHolder2>(0, click()))
        intended(hasComponent(ActivityList::class.java.name))
        pressBack()
    }

    @Test
    fun checkToastSameName(){
        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.etDialog)).perform(typeText(listTest), closeSoftKeyboard())
        onView(withId(android.R.id.button1)).perform(click())
        onView(ViewMatchers.withText(R.string.name_exists)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(activityRule.activity.window.decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}