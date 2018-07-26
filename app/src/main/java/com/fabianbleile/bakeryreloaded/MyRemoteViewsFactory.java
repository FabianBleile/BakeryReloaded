package com.fabianbleile.bakeryreloaded;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.fabianbleile.bakeryreloaded.R;
import com.fabianbleile.bakeryreloaded.Room.IngredientDatabase;
import com.fabianbleile.bakeryreloaded.Room.IngredientWidget;
import com.fabianbleile.bakeryreloaded.Utils.RecipeObject;

import java.util.ArrayList;
import java.util.List;

class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    static List<IngredientWidget> mIngredientList;

    public MyRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        IngredientDatabase database = IngredientDatabase.getDatabase(mContext);
        mIngredientList = database.contactDao().getAll();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredientList == null ? 0 : mIngredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        IngredientWidget item = mIngredientList.get(i);
        RemoteViews rv = new RemoteViews(
                mContext.getPackageName(),
                R.layout.shopping_list_widget_item)
                ;
        rv.setTextViewText(R.id.tv_ingredient, item.getIngredient());
        rv.setTextViewText(R.id.tv_quantity, String.valueOf(item.getQuantity()));
        rv.setTextViewText(R.id.tv_measure, item.getMeasure());
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
