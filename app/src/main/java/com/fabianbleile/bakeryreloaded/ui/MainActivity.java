package com.fabianbleile.bakeryreloaded.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fabianbleile.bakeryreloaded.R;
import com.fabianbleile.bakeryreloaded.Room.IngredientDatabase;
import com.fabianbleile.bakeryreloaded.Room.IngredientWidget;
import com.fabianbleile.bakeryreloaded.ShoppingListWidget;
import com.fabianbleile.bakeryreloaded.Utils.RecipeObject;
import com.fabianbleile.bakeryreloaded.Utils.JsonUtils;
import com.fabianbleile.bakeryreloaded.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    public static final String TAG = "TAG";
    private static Context mContext;
    private ArrayList<RecipeObject> mRecipeData;
    public static RecipeObject mRecipeObject;
    private Loader<ArrayList<RecipeObject>> mRecipeLoader;
    private RecipeAdapter mRecipeAdapter;
    private ConstraintLayout constraintLayout;
    private IngredientDatabase mIngredientDatabase;

    @Override
    public void onClick(View view) {
        mRecipeObject = mRecipeData.get(view.getId());
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.setAction(Intent.ACTION_ATTACH_DATA);
        intent.putExtra(recipeStepDetailFragment.ARG_RECIPE_NAME, mRecipeObject.getName());
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        notifyChangeDesiredRecipe(view.getId());
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mIngredientDatabase = IngredientDatabase.getDatabase(this.getApplication());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mRecipeAdapter = new RecipeAdapter(getApplicationContext(), this, this);
        recyclerView.setAdapter(mRecipeAdapter);
        constraintLayout = findViewById(R.id.constraintLayout);

        if(isNetworkAvailable()){
            LoaderManager.LoaderCallbacks<ArrayList<RecipeObject>> mRecipeLoaderManager = new LoaderManager.LoaderCallbacks<ArrayList<RecipeObject>>() {
                @NonNull
                @Override
                public Loader<ArrayList<RecipeObject>> onCreateLoader(int i, @Nullable Bundle bundle) {
                    return new loadDataAsyncTask(getApplicationContext());
                }

                @Override
                public void onLoadFinished(@NonNull Loader<ArrayList<RecipeObject>> loader, ArrayList<RecipeObject> recipeList) {
                    mRecipeData = recipeList;
                    if (mRecipeAdapter != null) {
                        mRecipeAdapter.setData(mRecipeData);
                    }

                    // first run
                    if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("firstrun", true)) {
                        // Do first run stuff here then set 'firstrun' as false

                        notifyChangeDesiredRecipe(0);

                        // using the following line to edit/commit prefs
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("firstrun", false).apply();
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<ArrayList<RecipeObject>> loader) {
                    Log.e(TAG, "in on reset");
                    mRecipeAdapter.setData(null);
                }
            };

            getSupportLoaderManager().initLoader(0, null, mRecipeLoaderManager);
        } else {
            Toast.makeText(mContext, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }

    void insertAll(int desiredRecipeId){
        List<IngredientWidget> mIngredientList = new ArrayList<>();

        RecipeObject recipeObject = mRecipeData.get(desiredRecipeId);
        ArrayList<RecipeObject.IngredientObject> ingredientObjects = recipeObject.getIngredients();

        for (int j = 0; j < ingredientObjects.size(); j++) {
            RecipeObject.IngredientObject ingredientObject = ingredientObjects.get(j);
            int quantity = ingredientObject.getQuantity();
            String measure = ingredientObject.getMeasure();
            String ingredient = ingredientObject.getIngredient();

            IngredientWidget ingredientWidget = new IngredientWidget(quantity, measure, ingredient);
            mIngredientList.add(ingredientWidget);
        }

        new insertAllAsyncTask(mIngredientDatabase).execute(mIngredientList);
    }

    private static class insertAllAsyncTask extends AsyncTask<List<IngredientWidget>, Void, Void> {
        private IngredientDatabase db;

        insertAllAsyncTask(IngredientDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(List<IngredientWidget>... lists) {
            Log.e("insertAllAsyncTask", lists.toString() + "     "+db);
            db.contactDao().deleteAll();
            db.contactDao().insertAll(lists[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                    new ComponentName(mContext, ShoppingListWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listView);
            Log.e(MainActivity.TAG, "appWidgetManager updated");
        }
    }


    private static class loadDataAsyncTask extends AsyncTaskLoader<ArrayList<RecipeObject>> {

        private loadDataAsyncTask(@NonNull Context context) {
            super(context);
        }

        ArrayList<RecipeObject> mRecipeList;

        @Nullable
        @Override
        public ArrayList<RecipeObject> loadInBackground() {
            String networkResponse = "";
            try {
                URL url = NetworkUtils.buildUrl();
                networkResponse = NetworkUtils.getResponseFromHttpsUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<JSONObject> recipeJSONObjectList = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(networkResponse);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    recipeJSONObjectList.add(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mRecipeList = new ArrayList<>();
            for (int i = 0; i < recipeJSONObjectList.size(); i++) {
                JSONObject jsonObject = recipeJSONObjectList.get(i);
                RecipeObject recipeObject = JsonUtils.fromJson(jsonObject);
                mRecipeList.add(recipeObject);
            }

            return (mRecipeList);
        }

        @Override
        public void deliverResult(@Nullable ArrayList<RecipeObject> data) {
            if(isReset()){
                if (data != null) {

                    releaseResources(data);

                    return;

                }
            }
            ArrayList<RecipeObject> oldRecipes = mRecipeList;
            mRecipeList = data;

            if(isStarted()){
                super.deliverResult(data);
            }
            if(oldRecipes != null && oldRecipes != data){
                releaseResources(oldRecipes);
            }
        }
        @Override
        protected void onStartLoading() {
            if (mRecipeList != null) {
                deliverResult(mRecipeList);
            }

            if (takeContentChanged()) {
                forceLoad();
            } else if (mRecipeList == null) {
                forceLoad();
            }
        }

        @Override
        protected void onStopLoading() {
            cancelLoad();
        }

        @Override
        protected void onReset() {
            // Ensure the loader is stopped.
            onStopLoading();
            if (mRecipeList != null) {
                releaseResources(mRecipeList);
                mRecipeList = null;
            }
        }

        @Override
        public void onCanceled(@Nullable ArrayList<RecipeObject> data) {
            super.onCanceled(data);
            releaseResources(data);
        }

        @Override
        protected void onForceLoad() {
            super.onForceLoad();
        }

        private void releaseResources(ArrayList<RecipeObject> data) {
            // for an array list this is just for my future me
        }
    }


    private void notifyChangeDesiredRecipe(int id) {
        if(mRecipeData != null){
            for (int i = 0; i < mRecipeData.size(); i++) {
                if(i == id){
                    setDefaults("desiredRecipe",String.valueOf(id), this);
                    showSnackbarForDefaultFile(id);
                    insertAll(id);
                    return;
                }
            }
        }
    }
    private void showSnackbarForDefaultFile(int id) {
        Snackbar snackbar = Snackbar
                .make(constraintLayout, mRecipeData.get(id).getName() + " set to desired Recipe. View in widget", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //----------------------------------------------------------------------------------------------
    // helper methods
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    //-------------------------------------------------------------------------------------------------------------------
    //This part handles all the storage in SharedPreferences
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
