package com.example.blog_javacode.people;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.VPAdapter;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class PersonDetailActivity extends AppCompatActivity {
    private String account_main;
    private String account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        TextView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());


        getData();
        setuptab();

    }

    private void getData(){
        //lay du lieu tu man hinh truoc
        account_main = getIntent().getStringExtra("account_main");
        account = getIntent().getStringExtra("account");
        String name = getIntent().getStringExtra("name");
        String view = getIntent().getStringExtra("view");
        String prodn = getIntent().getStringExtra("prodn");
        String postn = getIntent().getStringExtra("postn");
        String intro = getIntent().getStringExtra("introduce");
        String avatar = getIntent().getStringExtra("avatar");

        //anh xa
        TextView idTVNamePersonDetail = findViewById(R.id.idTVNamePersonDetail);
        TextView idTVViewPersonDetail = findViewById(R.id.idTVViewPersonDetail);
        TextView idTVPostNPersonDetail = findViewById(R.id.idTVPostNPersonDetail);
        TextView idTVProdNPersonDetail = findViewById(R.id.idTVProdNPersonDetail);
        TextView idTVIntroPersonDetail = findViewById(R.id.idTVIntroPersonDetail);
        ImageView idIVPersonDetail = findViewById(R.id.idIVPersonDetail);


        //setText
        idTVNamePersonDetail.setText(name);
        idTVViewPersonDetail.setText(view);
        idTVPostNPersonDetail.setText(postn);
        idTVProdNPersonDetail.setText(prodn);
        idTVIntroPersonDetail.setText(intro);
        Picasso.get().load(APIUtils.Base_Url_image+ avatar).into(idIVPersonDetail);



    }

    private void setuptab(){
        TabLayout tabLayout;
        ViewPager viewPager;
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new PostPersonDetailFragment(account,account_main,""),"");
        vpAdapter.addFragment(new ProductPersonDetailFragment(account),"");


        tabLayout = findViewById(R.id.idtablayoutperson);
        viewPager = findViewById(R.id.viewpagerpersondetail);
        viewPager.setAdapter(vpAdapter);

        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_baseline_article_24);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_baseline_store_24);
    }

}