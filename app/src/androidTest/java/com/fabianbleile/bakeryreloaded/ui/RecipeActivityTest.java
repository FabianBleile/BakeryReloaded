package com.fabianbleile.bakeryreloaded.ui;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.fabianbleile.bakeryreloaded.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void clickRecyclerViewItem_OpensRecipeActivity() {
        /**
         * Click on one element in Recycler View an check that
         * the right ingredients list is opened by RecipeActiviy.
         */

        // Pick first element of Recycler View (Nutella Pie) an click it.
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        // Check that the ingredient list contains one specific part of Nutella Pie.
        onView(withId(R.id.ingredient_list)).check(matches(hasDescendant(withText("Graham Cracker crumbs"))));
        

    }

}
