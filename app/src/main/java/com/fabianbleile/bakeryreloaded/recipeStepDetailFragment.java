package com.fabianbleile.bakeryreloaded;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fabianbleile.bakeryreloaded.dummy.DummyContent;
import com.google.gson.Gson;

/**
 * A fragment representing a single recipeStep detail screen.
 * This fragment is either contained in a {@link recipeStepListActivity}
 * in two-pane mode (on tablets) or a {@link recipeStepDetailActivity}
 * on handsets.
 */
public class recipeStepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the step object that this fragment
     * represents.
     */
    public static final String ARG_STEP_OBJECT = "step";
    public static final String ARG_RECIPE_NAME = "name";

    private RecipeObject.StepObject mStepObject;
    private String mRecipeName;

    public recipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP_OBJECT)) {
            Bundle arguments = getArguments();
            Gson gson = new Gson();
            mStepObject = gson.fromJson(arguments.getString(ARG_STEP_OBJECT), RecipeObject.StepObject.class);
            mRecipeName = arguments.getString(ARG_RECIPE_NAME);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mStepObject.shortDescription);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipestep_detail, container, false);

        if (mStepObject != null) {
            ((TextView) rootView.findViewById(R.id.recipestep_detail)).setText(mStepObject.description);
        }

        return rootView;
    }
}
