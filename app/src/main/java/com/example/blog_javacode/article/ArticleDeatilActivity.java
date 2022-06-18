package com.example.blog_javacode.article;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;

import android.widget.ImageView;
import android.widget.TextView;


import com.example.blog_javacode.MyGesture;
import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class ArticleDeatilActivity extends AppCompatActivity {
    String header;
    String category;
    String body;
    String images;
    List<String> fimage;
    String time;
    GestureDetector gestureDetector;


    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_deatil);
        setContentView(R.layout.activity_article_deatil);
        header = getIntent().getStringExtra("header");
        category = getIntent().getStringExtra("category");
        body = getIntent().getStringExtra("body");
        images = getIntent().getStringExtra("images");
        time = getIntent().getStringExtra("time");


        TextView idTVHeading = findViewById(R.id.idTVHeading);
        TextView idTVCategory = findViewById(R.id.idTVCategory);
        TextView idTVBody = findViewById(R.id.idTVBody);
        ImageView imageView = findViewById(R.id.idIVArticleDeatil);
        TextView idpositionIV = findViewById(R.id.idpositionIV);
        TextView btnBack = findViewById(R.id.btnBack);


        idTVHeading.setText(header);
        idTVCategory.setText(category);
        idTVBody.setText(body);

        btnBack.setOnClickListener(view ->{
            finish();
        });

        fimage = Arrays.asList(images.split("\\|"));
        Picasso.get().load(APIUtils.Base_Url_image + fimage.get(0)).into(imageView);
        idpositionIV.setText("1/"+ fimage.size());


        gestureDetector = new GestureDetector(this,new MyGesture(fimage, imageView, idpositionIV));

        imageView.setOnTouchListener((view, motionEvent) -> {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        });

    }


}