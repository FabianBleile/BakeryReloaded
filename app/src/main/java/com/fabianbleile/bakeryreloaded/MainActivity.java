package com.fabianbleile.bakeryreloaded;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "TAG";
    private Context mContext;
    private ArrayList<RecipeObject> mRecipeData;
    public static RecipeObject mRecipeObject;
    private Loader<ArrayList<RecipeObject>> mRecipeLoader;
    private RecipeAdapter mRecipeAdapter;
    private LoaderManager.LoaderCallbacks<ArrayList<RecipeObject>> mRecipeLoaderManager;

    @Override
    public void onClick(View view) {
        Toast.makeText(mContext, view.getId() + " was clicked", Toast.LENGTH_SHORT).show();
        mRecipeObject = mRecipeData.get(view.getId());
        Intent intent = new Intent(this, recipeStepListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mRecipeAdapter = new RecipeAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(mRecipeAdapter);

        mRecipeLoaderManager = new LoaderManager.LoaderCallbacks<ArrayList<RecipeObject>>() {
                @NonNull
                @Override
                public Loader<ArrayList<RecipeObject>> onCreateLoader(int i, @Nullable Bundle bundle) {
                    return new loadDataAsyncTask(getApplicationContext());
                }

                @Override
                public void onLoadFinished(@NonNull Loader<ArrayList<RecipeObject>> loader, ArrayList<RecipeObject> recipeList) {
                    mRecipeData = recipeList;
                    if(mRecipeAdapter != null){
                        mRecipeAdapter.setData(mRecipeData);
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<ArrayList<RecipeObject>> loader) {
                    Log.e(TAG, "in on reset");
                    mRecipeAdapter.setData(null);
                }
            };

        getSupportLoaderManager().initLoader(0, null, mRecipeLoaderManager);
    }


    private static class loadDataAsyncTask extends AsyncTaskLoader<ArrayList<RecipeObject>> {

        public loadDataAsyncTask(@NonNull Context context) {
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

    //----------------------------------------------------------------------------------------------
    // helper methods
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
