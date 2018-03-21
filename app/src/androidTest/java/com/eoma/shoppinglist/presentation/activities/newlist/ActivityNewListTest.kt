package com.eoma.shoppinglist.presentation.activities.newlist

import android.content.Intent
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.contrib.RecyclerViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.support.test.rule.ActivityTestRule
import android.support.v7.widget.RecyclerView
import com.eoma.shoppinglist.R
import com.eoma.shoppinglist.presentation.recycler.*
import com.eoma.shoppinglist.sqlite.EntityLists
import com.eoma.shoppinglist.sqlite.EntityProducts
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ActivityNewListTest {

    @get:Rule
    val activityRule = ActivityTestRule<ActivityNewList>(ActivityNewList::class.java, true, false)
    private lateinit var adapter1: RecyclerAdapter2<EntityProducts>
    private lateinit var adapter2: RecyclerAdapter2<EntityLists>

    @Before
    fun setUp(){

        val intent = Intent()
        intent.putExtra("listname", "Test")
        activityRule.launchActivity(intent)

        adapter1 =
                activityRule.activity.findViewById<RecyclerView>(R.id.recycler1).adapter as RecyclerAdapter2<EntityProducts>
        adapter2 =
                activityRule.activity.findViewById<RecyclerView>(R.id.recycler2).adapter as RecyclerAdapter2<EntityLists>



        for (i in 0..2) {
            onView(withId(R.id.recycler1)).perform(actionOnItemAtPosition<RecyclerViewHolder2>(i, click()))
        }
    }

    @Test
    fun onCreate() {

        for (i in 0..2) {
            onView(withId(R.id.recycler2)).check(matches(hasDescendant(withText(adapter1.getItem(i).product))))
        }
    }

    @Test
    fun createNewProductAndSwipeRightBoth(){

        //CreateNewProduct
        val tot1 = adapter1.itemCount
        onView(withId(R.id.fabNewProduct)).perform(click())
        onView(withId(R.id.etDialog)).perform(typeText("Coffee"), closeSoftKeyboard())
        onView(withId(android.R.id.button1)).perform(click())
        assertEquals(tot1+1, adapter1.itemCount)


        //Swipe recycler1
        val tot2 = adapter2.itemCount
        onView(withId(R.id.recycler1))
                .perform(actionOnItemAtPosition<RecyclerViewHolder2>(tot1-1, swipeRight()))
        assertEquals( tot1, adapter1.itemCount)

        //onView(withId(R.id.recycler2))
                //.perform(scrollToPosition<RecyclerViewHolder2>(tot2-1))

        //Swipe recycler2
        onView(withId(R.id.recycler2))
                .perform(actionOnItemAtPosition<RecyclerViewHolder2>(tot2-1, swipeRight()))
        assertEquals( tot2-1,adapter2.itemCount)
    }
}


