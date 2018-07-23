package com.fabianbleile.bakeryreloaded.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fabianbleile.bakeryreloaded.R;
import com.fabianbleile.bakeryreloaded.Utils.RecipeObject;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {

    Context mContext;
    ArrayList<RecipeObject> recipeList = new ArrayList<>();
    private View.OnClickListener mOnClicklistener;
    private View.OnLongClickListener mOnLongClicklistener;

    public RecipeAdapter(Context context, View.OnClickListener onClickListener, View.OnLongClickListener onLongClicklistener){
        mContext = context;
        mOnClicklistener = onClickListener;
        mOnLongClicklistener = onLongClicklistener;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_recipe_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        String recipeName = "";
        try {
            recipeName = recipeList.get(position).name;
            Log.e(MainActivity.TAG, recipeName);
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        recyclerViewHolder.textView.setText(recipeName);

        recyclerViewHolder.itemView.setOnClickListener(mOnClicklistener);
        recyclerViewHolder.itemView.setOnLongClickListener(mOnLongClicklistener);
        recyclerViewHolder.itemView.setId(position);

        URL url = null;
        try {
            url = new URL(getRecipeImage(position));
        } catch (MalformedURLException ignored) {
        }
        if (url != null)
        Picasso.get()
                .load(String.valueOf(url))
                .resize(4000, 1200)
                .centerCrop()
                .into(recyclerViewHolder.imageView);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_recipe);
            imageView = itemView.findViewById(R.id.iv_recipe);
        }
    }

    public void setData(ArrayList<RecipeObject> recipeList){
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    private String getRecipeImage(int position){
        String[] recipeImageStringArray  = {
                mContext.getResources().getString(R.string.recipe1),
                mContext.getResources().getString(R.string.recipe2),
                mContext.getResources().getString(R.string.recipe3),
                mContext.getResources().getString(R.string.recipe4)
        };

        return recipeImageStringArray[position];
    }
}
