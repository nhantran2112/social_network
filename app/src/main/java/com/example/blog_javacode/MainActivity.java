package com.example.blog_javacode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.blog_javacode.article.ArticleFragment;
import com.example.blog_javacode.home.HomeFragment;
import com.example.blog_javacode.home.SearchActivity;
import com.example.blog_javacode.people.Other_personFragment;
import com.example.blog_javacode.person.PersonFragment;
import com.example.blog_javacode.store.StoreFragment;
import com.google.android.material.tabs.TabLayout;

import java.net.URISyntaxException;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.client.Socket;


public class MainActivity extends AppCompatActivity {
    public static Socket mSocket;
    private String account, password, name, intro, view, prodnumber, postnumber, avatar, token;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Bấm lại nút Back để thoát ứng dụng", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 3000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView btnlogout = findViewById(R.id.buttonLogout);
        TextView btnSearch = findViewById(R.id.buttonSearch);

        btnlogout.setOnClickListener(view -> {
            mSocket.disconnect();
            finish();
        });

        btnSearch.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        try {
            mSocket = IO.socket("https://nhantransocial.herokuapp.com/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
        getDataLogin();

        setuptab();
    }


    private void setuptab() {
        TabLayout tabLayout;
        ViewPager viewPager;
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new HomeFragment(account, name, avatar), "");
        vpAdapter.addFragment(new PersonFragment(account, password, name, intro, view, prodnumber, postnumber, avatar), "");
        vpAdapter.addFragment(new Other_personFragment(account,""), "");
        vpAdapter.addFragment(new StoreFragment(""), "");
        vpAdapter.addFragment(new ArticleFragment(""), "");

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(vpAdapter);
        if (token != null)
            viewPager.setCurrentItem(1);

        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_baseline_home_24);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_baseline_person_24);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_baseline_people_24);
        Objects.requireNonNull(tabLayout.getTabAt(3)).setIcon(R.drawable.ic_baseline_store_24);
        Objects.requireNonNull(tabLayout.getTabAt(4)).setIcon(R.drawable.ic_baseline_article_24);
    }

    private void getDataLogin() {
        account = getIntent().getStringExtra("account");
        password = getIntent().getStringExtra("password");
        name = getIntent().getStringExtra("name");
        intro = getIntent().getStringExtra("introduce");
        view = getIntent().getStringExtra("views");
        prodnumber = getIntent().getStringExtra("prodn");
        postnumber = getIntent().getStringExtra("postn");
        avatar = getIntent().getStringExtra("avatar");
        token = getIntent().getStringExtra("token");
    }


}