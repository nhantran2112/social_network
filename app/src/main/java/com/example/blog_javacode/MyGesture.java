package com.example.blog_javacode;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.blog_javacode.Retrofit2.APIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyGesture extends GestureDetector.SimpleOnGestureListener {
    private final List<String> listimage;
    private final ImageView imageView;
    private final TextView tvposition;

    public MyGesture(List<String> listimage, ImageView imageView , TextView tvposition) {
        this.listimage = listimage;
        this.imageView = imageView;
        this.tvposition = tvposition;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        String a = (String) tvposition.getText();
        String[] b = a.split("/");
        int position = Integer.parseInt(b[0]) - 1;
        Log.d("CCCC", e1.getX() +"-"+ e2.getX() +"-"+ Math.abs(velocityX));
        //Keo tu trai sang phai
        int SWIPE_THRESHOLD = 100;
        int SWIPE_VELOCITY_THRESHOLD = 0;
        if(e2.getX() - e1.getX() > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
            position--;
            if(position <0){
                position =0;
            }
            Log.d("CCCC",listimage.get(position) );
            Picasso.get().load(APIUtils.Base_Url_image + listimage.get(position)).into(imageView);
            tvposition.setText((position + 1) +"/"+ listimage.size());

        }
        else if(e1.getX() - e2.getX() > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
            position++;
            if(position >listimage.size()-1){
                position =listimage.size()-1;
            }
            Log.d("CCCC",listimage.get(position) );
            Picasso.get().load(APIUtils.Base_Url_image+ listimage.get(position)).into(imageView);
            tvposition.setText((position + 1) +"/"+ listimage.size());
        }

        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
