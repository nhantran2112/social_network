package com.example.blog_javacode.article;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArticleRVAdapter extends RecyclerView.Adapter<ArticleRVAdapter.ViewHolder> {
    private final ArrayList<Article> articleArrayList;
    private final Context context;

    public ArticleRVAdapter(ArrayList<Article> articleArrayList, Context context) {
        this.articleArrayList = articleArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ArticleRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_rv_item, parent,false);
        return new ArticleRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleRVAdapter.ViewHolder holder, int position) {
        Article article = articleArrayList.get(position);
        holder.idTVArticleHeading.setText(article.getHeaders());
        holder.idTVSubArticle.setText(article.getCategory());
        Picasso.get().load(APIUtils.Base_Url_image+article.getFirstImage()).into(holder.idIVArticle);
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context,ArticleDeatilActivity.class);
            i.putExtra("header" , article.getHeaders() );
            i.putExtra("category" , article.getCategory() );
            i.putExtra("body" , article.getBody() );
            i.putExtra("images" , article.getImages() );
            i.putExtra("time" , article.getTimes() );

            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView idIVArticle ;
        private final TextView idTVSubArticle;
        private final TextView idTVArticleHeading;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTVArticleHeading = itemView.findViewById(R.id.idTVArticleHeading);
            idTVSubArticle = itemView.findViewById(R.id.idTVSubArticle);
            idIVArticle = itemView.findViewById(R.id.idIVArticle);
        }
    }
}
