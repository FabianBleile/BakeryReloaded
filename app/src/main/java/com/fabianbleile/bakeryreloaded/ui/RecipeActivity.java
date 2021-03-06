package com.fabianbleile.bakeryreloaded.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fabianbleile.bakeryreloaded.R;
import com.fabianbleile.bakeryreloaded.Utils.RecipeObject;

import java.util.ArrayList;

/**
 * An activity representing a list of recipeSteps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link recipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeActivity extends AppCompatActivity {

    private boolean mTwoPane; // Tablet Mode
    private static RecipeObject mRecipeObject;
    private static String mTitle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                Intent intent = new Intent(this, MainActivity.class);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipeObject = MainActivity.mRecipeObject;

        if (findViewById(R.id.recipestep_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView mStepRecyclerView = findViewById(R.id.recipestep_list);
        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mStepRecyclerView.setLayoutManager(stepLayoutManager);
        mStepRecyclerView.setAdapter(new StepRecyclerViewAdapter(this, mRecipeObject.getSteps(), mTwoPane));

        RecyclerView mIngredientRecyclerView = findViewById(R.id.ingredient_list);
        GridLayoutManager ingredientLayoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        mIngredientRecyclerView.setLayoutManager(ingredientLayoutManager);
        mIngredientRecyclerView.setAdapter(new IngredientRecyclerAdapter(this, mRecipeObject.getIngredients(), mTwoPane));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TextUtils.equals(getIntent().getAction(), Intent.ACTION_ATTACH_DATA)){
            mTitle = getIntent().getStringExtra(recipeStepDetailFragment.ARG_RECIPE_NAME);
            setTitle(mTitle);
        }
    }

    public static RecipeObject.StepObject returnNewStepObject(int currentStepObjectId, boolean next){
        // boolean next, if true next was clicked, if false previous was clicked
        int stepLength = mRecipeObject.getSteps().size();
        if(next){
            if (currentStepObjectId + 1 < (stepLength)){
                int newStepObjectId = currentStepObjectId + 1;
                RecipeObject.StepObject newStepObject = mRecipeObject.getSteps().get(newStepObjectId);
                newStepObject.setId(newStepObjectId);
                return newStepObject;
            } else {
                // end reached
                return new RecipeObject.StepObject(-1, null,null,null,null);
            }
        } else {
            if (!(currentStepObjectId - 1 < 0)) {
                int newStepObjectId = currentStepObjectId - 1;
                RecipeObject.StepObject newStepObject = mRecipeObject.getSteps().get(newStepObjectId);
                newStepObject.setId(newStepObjectId);
                return newStepObject;
            } else {
                // start reached
                return new RecipeObject.StepObject(-2, null,null,null,null);
            }
        }

    }

    //---------------------------------------------------------------------------------------------------------------
    // all I need to implement in RecipeActivity as well for twoPane Mode
    public void onClickPrevious(View v) {
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
        int currentStepObjectId = recipeStepDetailFragment.getmStepId();
        RecipeObject.StepObject newStepObject = RecipeActivity.returnNewStepObject(currentStepObjectId, true);
        if(newStepObject.getId() == - 1){
            // only used on narrow width devices
            Toast.makeText(this, "Enjoy!", Toast.LENGTH_SHORT).show();
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

    //******************************//
    //                              //
    //      Recipe Step Adapter     //
    //                              //
    //******************************//
    public static class StepRecyclerViewAdapter
            extends RecyclerView.Adapter<StepRecyclerViewAdapter.ViewHolder> {

        private final RecipeActivity mParentActivity;
        private final ArrayList<RecipeObject.StepObject> mSteps;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeObject.StepObject stepObject = (RecipeObject.StepObject) view.getTag();
                if (mTwoPane) { //Tablet Mode
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(recipeStepDetailFragment.ARG_STEP_OBJECT, stepObject);
                    arguments.putString(recipeStepDetailFragment.ARG_RECIPE_NAME, mTitle);
                    recipeStepDetailFragment fragment = new recipeStepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipestep_detail_container, fragment)
                            .commit();
                } else { // SmartphoneMode
                    Context context = view.getContext();
                    Intent intent = new Intent(context, recipeStepDetailActivity.class);
                    intent.putExtra(recipeStepDetailFragment.ARG_STEP_OBJECT, stepObject);
                    intent.putExtra(recipeStepDetailFragment.ARG_RECIPE_NAME, mTitle);

                    context.startActivity(intent);
                }
            }
        };

        StepRecyclerViewAdapter(RecipeActivity parent,
                                      ArrayList<RecipeObject.StepObject> steps,
                                      boolean twoPane) {
            mSteps = steps;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipestep_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            int stepIdInt = mSteps.get(position).getId();
            String stepIdString = " ";
            if (stepIdInt == 0){
                stepIdString = "";
            } else {
                stepIdString = String.valueOf(stepIdInt);
            }
            holder.mIdView.setText(stepIdString);
            holder.mShortDescriptionView.setText(mSteps.get(position).getShortDescription());

            holder.itemView.setTag(mSteps.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mShortDescriptionView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mShortDescriptionView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

    //******************************//
    //                              //
    //  Recipe Ingredient Adapter   //
    //                              //
    //******************************//
    public static class IngredientRecyclerAdapter
            extends RecyclerView.Adapter<IngredientRecyclerAdapter.ViewHolder> {

        private final RecipeActivity mParentActivity;
        private final ArrayList<RecipeObject.IngredientObject> mIngredients;
        private final boolean mTwoPane;

        IngredientRecyclerAdapter(RecipeActivity parent,
                                ArrayList<RecipeObject.IngredientObject> ingredients,
                                boolean twoPane) {
            mIngredients = ingredients;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            RecipeObject.IngredientObject ingredientObject = mIngredients.get(position);
            holder.tvIngredient.setText(ingredientObject.getIngredient());
            holder.tvMeasure.setText(ingredientObject.getMeasure());
            holder.tvQuantity.setText(String.valueOf(ingredientObject.getQuantity()));

            holder.itemView.setTag(mIngredients.get(position));
        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvIngredient;
            TextView tvMeasure;
            TextView tvQuantity;

            ViewHolder(View view) {
                super(view);
                tvIngredient = (TextView) view.findViewById(R.id.tv_ingredient);
                tvMeasure = (TextView) view.findViewById(R.id.tv_measure);
                tvQuantity = (TextView) view.findViewById(R.id.tv_quantity);
            }
        }
    }
}
