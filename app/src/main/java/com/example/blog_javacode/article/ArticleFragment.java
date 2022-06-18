package com.example.blog_javacode.article;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleFragment extends Fragment implements CategoryRVAdapter.CategoryClickInterface {

    private ArrayList<Article> articleArrayList;
    private ArticleRVAdapter articleRVAdapter;

    private ArrayList<String> categoryArrayList;
    private CategoryRVAdapter categoryRVAdapter;

    private SwipeRefreshLayout idSRL;

    private final String datasearch;

    public ArticleFragment(String datasearch) {
        this.datasearch = datasearch;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        RecyclerView idRVArticle = view.findViewById(R.id.idRVArticle);
        articleArrayList = new ArrayList<>();
        articleRVAdapter = new ArticleRVAdapter(articleArrayList , this.getActivity());
        idRVArticle.setLayoutManager(new LinearLayoutManager( this.getActivity()));
        idRVArticle.setAdapter(articleRVAdapter);
        getArticles("Tất cả");
        articleRVAdapter.notifyDataSetChanged();

        RecyclerView idRVCategoriesArticle = view.findViewById(R.id.idRVCategoriesArticle);
        categoryArrayList = new ArrayList<>();
        categoryRVAdapter = new CategoryRVAdapter(categoryArrayList, this.getActivity(), this::onCategoryClick);
        idRVCategoriesArticle.setAdapter(categoryRVAdapter);
        categoryRVAdapter.notifyDataSetChanged();

        idSRL = view.findViewById(R.id.idSRLAF);

        idSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getArticles("Tất cả");
                        idSRL.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        return  view;
    }

    private void getArticles(String category){
        articleArrayList.clear();
        DataClient dataClient = APIUtils.getData();
        Call<ArrayList<Article>> callback = dataClient.getArticles(datasearch);
        callback.enqueue(new Callback<ArrayList<Article>>() {
            @Override
            public void onResponse(Call<ArrayList<Article>> call, Response<ArrayList<Article>> response) {
                ArrayList<Article> modal = response.body();
                if(category.equals("Tất cả")){
                    for (int i=0 ; i < modal.size();i++){
                        Article article = new Article(modal.get(i).getId(), modal.get(i).getAccount(),
                                modal.get(i).getHeaders(), modal.get(i).getCategory(),
                                modal.get(i).getBody(), modal.get(i).getImages(), modal.get(i).getTimes());
                        articleArrayList.add(article);
                    }
                }
                else{
                    for (int i=0 ; i < modal.size();i++){
                        if(modal.get(i).getCategory().equals(category)) {
                            Article article = new Article(modal.get(i).getId(), modal.get(i).getAccount(),
                                    modal.get(i).getHeaders(), modal.get(i).getCategory(),
                                    modal.get(i).getBody(), modal.get(i).getImages(), modal.get(i).getTimes());
                            articleArrayList.add(article);
                        }

                    }
                }
                if(categoryArrayList.isEmpty()){
                    categoryArrayList.add("Tất cả");
                    for(int i =1 ; i<articleArrayList.size() ; i++){

                        if(categoryArrayList.size()==1){
                            categoryArrayList.add(modal.get(0).getCategory());
                        }
                        else{
                            for(int j =0 ; j<categoryArrayList.size(); j++){
                                if(modal.get(i).getCategory().equals(categoryArrayList.get(j))){
                                    break;
                                }
                                if(j == categoryArrayList.size()-1)
                                    categoryArrayList.add(modal.get(i).getCategory());
                            }
                        }
                    }
                }
                categoryRVAdapter.notifyDataSetChanged();
                articleRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Article>> call, Throwable t) {
                Log.d("Error_getArticle" , t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryArrayList.get(position);
        getArticles(category);
    }
}