package com.example.blog_javacode.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {
    private String idpost;
    private String account;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        idpost = getIntent().getStringExtra("idpost");
        account = getIntent().getStringExtra("account");
        String name = getIntent().getStringExtra("name");
        String avatar = getIntent().getStringExtra("avatar");
        String text = getIntent().getStringExtra("text");
        String time = getIntent().getStringExtra("time");
        String link = getIntent().getStringExtra("link");

        ImageView idIVAvaPersonPost = findViewById(R.id.idIVAvaPersonPost);
        TextView idTVNamePersonPost = findViewById(R.id.idTVNamePersonPost);
        TextView idTVTimePost = findViewById(R.id.idTVTimePost);
        TextView idTVTextPost = findViewById(R.id.idTVTextPost);
        TextView idTVReadCMT = findViewById(R.id.idTVReadCMT);
        Picasso.get().load(APIUtils.Base_Url_image+ avatar).into(idIVAvaPersonPost);
        idTVNamePersonPost.setText(name);
        idTVTextPost.setText(text);
        TimeAgo timeAgo = new TimeAgo();
        idTVTimePost.setText(timeAgo.covertTimeToText(time));


        idTVReadCMT.setOnClickListener(view -> {
            Intent i = new Intent(PostDetailActivity.this , CmtPostActivity.class);
            i.putExtra("idpost" , idpost);
            i.putExtra("account" , account);

            startActivity(i);
        });

        RecyclerView idRVImagePost = findViewById(R.id.idRVImagePost);
        List<String> fimage;
        if(link != null){
            fimage = Arrays.asList(link.split("\\|"));
            ImagePostRVAdapter imagePostRVAdapter = new ImagePostRVAdapter(fimage, R.layout.image_item_post, null);
            idRVImagePost.setAdapter(imagePostRVAdapter);
            imagePostRVAdapter.notifyDataSetChanged();
        }


    }
}