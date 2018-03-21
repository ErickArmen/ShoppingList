package com.eoma.shoppinglist.presentation.activities.list


import android.app.Activity
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import android.support.v7.widget.RecyclerView
import com.eoma.shoppinglist.R
import com.eoma.shoppinglist.presentation.activities.main.MainActivity
import com.eoma.shoppinglist.presentation.recycler.RecyclerAdapter2
import com.eoma.shoppinglist.presentation.recycler.RecyclerViewHolder2
import com.eoma.shoppinglist.sqlite.EntityListNames
import com.eoma.shoppinglist.sqlite.EntityLists
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ActivityListTest{

    @get:Rule
    val mainActivity = ActivityTestRule<MainActivity>(MainActivity::class.java, true, false)


    @Test
    fun completeProcess(){

        //Main
        mainActivity.launchActivity(Intent())
        val adaptermain = mainActivity.activity.findViewById<RecyclerView>(R.id.recyclerView).adapter as RecyclerAdapter2<EntityListNames>
        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.etDialog)).perform(typeText("TestList"), closeSoftKeyboard())
        onView(withId(android.R.id.button1)).perform(click())


        //NewList
        for (i in 0..2) {
            onView(withId(R.id.recycler1)).perform(actionOnItemAtPosition<RecyclerViewHolder2>(i, click()))
        }

        onView(withId(R.id.fab)).perform(click())


        val newListAct = getActivity()
        onView(ViewMatchers.withText(R.string.list_done)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(newListAct?.window?.decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))


        //Main
        onView(withId(R.id.recyclerView))
                .perform(actionOnItemAtPosition<RecyclerViewHolder2>(adaptermain.itemCount-1, click()))

        //List
        val listAct = getActivity()
        val adapter =  listAct?.findViewById<RecyclerView>(R.id.recyclerView)?.adapter as RecyclerAdapter2<EntityLists>
        val crossed = adapter.getItem(0).crossed
        onView(withId(R.id.recyclerView)).perform(actionOnItemAtPosition<RecyclerViewHolder2>(0, swipeRight()))
        assertEquals(!crossed, adapter.getItem(0).crossed)
        listAct.finish()

        //Main
        onView(withId(R.id.recyclerView))
                .perform(actionOnItemAtPosition<RecyclerViewHolder2>(adaptermain.itemCount-1, swipeRight()))
        onView(withId(android.R.id.button1)).perform(click())

    }

    private fun getActivity(): Activity? {

        val activity = arrayOfNulls<Activity>(2)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {

            val resumedAct = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
            val iter = resumedAct.iterator()
            activity[0] = iter.next()
        }

        return activity[0]
    }
}





