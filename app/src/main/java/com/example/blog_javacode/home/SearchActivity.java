package com.example.blog_javacode.home;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.blog_javacode.R;
import com.example.blog_javacode.VPAdapter;
import com.example.blog_javacode.article.ArticleFragment;
import com.example.blog_javacode.people.Other_personFragment;
import com.example.blog_javacode.people.PostPersonDetailFragment;
import com.example.blog_javacode.store.StoreFragment;
import com.google.android.material.tabs.TabLayout;

public class SearchActivity extends AppCompatActivity {

    private TextView btnBack, btnSearch;
    private EditText ETSearch;
    private String datasearch;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        anhxa();

        btnSearch.setOnClickListener(view -> {
            datasearch = ETSearch.getText().toString();
            if(datasearch.length() > 0){
                setuptab(datasearch);
            }
        });
        btnBack.setOnClickListener(view -> finish());
    }

    private void setuptab(String datasearch) {

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new PostPersonDetailFragment("","" ,datasearch ), "bài viết");
        vpAdapter.addFragment(new Other_personFragment("",datasearch), "mọi người");
        vpAdapter.addFragment(new StoreFragment(datasearch), "sản phẩm");
        vpAdapter.addFragment(new ArticleFragment(datasearch), "báo");


        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setAdapter(vpAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }
    private void anhxa(){
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        ETSearch = findViewById(R.id.ETSearch);
        tabLayout = findViewById(R.id.tablayoutsearch);
        viewPager = findViewById(R.id.viewpagersearch);
    }
}