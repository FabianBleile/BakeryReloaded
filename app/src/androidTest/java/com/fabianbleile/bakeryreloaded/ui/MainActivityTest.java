package com.fabianbleile.bakeryreloaded.ui;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.fabianbleile.bakeryreloaded.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkContentsOfCakeList() {
        /**
         * Checks that the main list of recipies contains all the different cakes.
         */
        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("Brownies"))));
        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("Yellow Cake"))));
        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("Cheesecake"))));

    }
}
