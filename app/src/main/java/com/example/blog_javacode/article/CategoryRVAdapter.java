package com.example.blog_javacode.article;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_javacode.R;

import java.util.ArrayList;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.ViewHolder> {
    private final ArrayList<String> categoryArrayList;
    private final Context context;
    private final CategoryClickInterface categoryClickInterface;

    public CategoryRVAdapter(ArrayList<String> categoryArrayList, Context context, CategoryClickInterface categoryClickInterface) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
        this.categoryClickInterface = categoryClickInterface;
    }

    @NonNull
    @Override
    public CategoryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_rv_item_article,parent,false);
        return new CategoryRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRVAdapter.ViewHolder holder, int position) {
        holder.idTVCategoryArticle.setText(categoryArrayList.get(position));
        holder.itemView.setOnClickListener(view -> categoryClickInterface.onCategoryClick(position));
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public interface CategoryClickInterface{
        void onCategoryClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView idTVCategoryArticle ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTVCategoryArticle = itemView.findViewById(R.id.idTVCategoryArticle);
        }
    }
}
