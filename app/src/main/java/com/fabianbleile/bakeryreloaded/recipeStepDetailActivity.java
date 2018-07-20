package com.fabianbleile.bakeryreloaded;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.fabianbleile.bakeryreloaded.Utils.RecipeObject;

/**
 * An activity representing a single recipeStep detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeActivity}.
 */
public class recipeStepDetailActivity extends AppCompatActivity{



    private static final String TAG = "recipeStepDetailActivity";
    Context mContext;
    private View v;
    public static RecipeObject.StepObject mStepObject;
    public static String mTitle;
    private boolean mFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_detail);

        mContext = getBaseContext();

        if (findViewById(R.id.app_bar) != null) {
            // The coordinator layout will be present only in the
            // small-screen layouts in portrait.
            // If this view is present, then the
            // activity should NOT be in full-screen mode.
            mFullScreen = false;
        } else {
            mFullScreen = true;
        }

        if (mFullScreen){
            Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
            setSupportActionBar(toolbar);

            // Show the Up button in the action bar.
            ActionBar actionBar = getSupportActionBar();if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } else {

        }

        // savedInstanceState is non-null when there is fragment state saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it. For more information, see the Fragments API guide at:
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Intent intent = getIntent();
            mStepObject = intent.getParcelableExtra(recipeStepDetailFragment.ARG_STEP_OBJECT);
            mTitle = intent.getStringExtra(recipeStepDetailFragment.ARG_RECIPE_NAME);

            Bundle arguments = new Bundle();
            arguments.putParcelable(recipeStepDetailFragment.ARG_STEP_OBJECT, mStepObject);
            arguments.putString(recipeStepDetailFragment.ARG_RECIPE_NAME, mTitle);
            recipeStepDetailFragment fragment = new recipeStepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipestep_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                Intent intent = new Intent(this, RecipeActivity.class);
                intent.setAction(Intent.ACTION_ATTACH_DATA);
                intent.putExtra(recipeStepDetailFragment.ARG_RECIPE_NAME, mTitle);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //---------------------------------------------------------------------------------------------------------------
    // all I need to implement in RecipeActivity as well for twoPane Mode
    public void onClickPrevious(View v) {
        // does something very interesting
        Toast.makeText(mContext, "TEST previous", Toast.LENGTH_SHORT).show();
        int currentStepObjectId = recipeStepDetailFragment.getmStepId();
        RecipeObject.StepObject newStepObject = RecipeActivity.returnNewStepObject(currentStepObjectId, false);
        if(newStepObject.getId() == - 2){
            // only used on narrow width devices
            finish();
        } else {
            Bundle arguments = new Bundle();
            arguments.putParcelable(recipeStepDetailFragment.ARG_STEP_OBJECT, newStepObject);
            Fragment fragment = new recipeStepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipestep_detail_container, fragment)
                    .commit();
        }
    }
    public void onClickNext(View v) {
        // does something very interesting
        Toast.makeText(mContext, "TEST next", Toast.LENGTH_SHORT).show();
        int currentStepObjectId = recipeStepDetailFragment.getmStepId();
        RecipeObject.StepObject newStepObject = RecipeActivity.returnNewStepObject(currentStepObjectId, true);
        if(newStepObject.getId() == - 1){
            // only used on narrow width devices
            Toast.makeText(mContext, "Enjoy!", Toast.LENGTH_SHORT).show();
        } else {
            Bundle arguments = new Bundle();
            arguments.putParcelable(recipeStepDetailFragment.ARG_STEP_OBJECT, newStepObject);
            Fragment fragment = new recipeStepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipestep_detail_container, fragment)
                    .commit();
        }
    }

}
