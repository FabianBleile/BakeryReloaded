package com.fabianbleile.bakeryreloaded;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {

    Context mContext;
    ArrayList<RecipeObject> recipeList = new ArrayList<>();
    private View.OnClickListener mOnClicklistener;

    public RecipeAdapter(Context context, View.OnClickListener onClickListener){
        mContext = context;
        mOnClicklistener = onClickListener;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_recipe, viewGroup, false));
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
        recyclerViewHolder.itemView.setId(position);
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

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_recipe);
        }
    }

    public void setData(ArrayList<RecipeObject> recipeList){
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }
}
