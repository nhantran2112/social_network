package com.example.blog_javacode.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.blog_javacode.R;

import java.util.ArrayList;

public class LinkShopLVAdapter extends BaseAdapter {

    private final ArrayList<LinkShop> linkShopArrayList;
    private final Context context;
    private final int layout;

    public LinkShopLVAdapter(ArrayList<LinkShop> linkShopArrayList, Context context, int layout) {
        this.linkShopArrayList = linkShopArrayList;
        this.context = context;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return linkShopArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return linkShopArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(layout,null);

        LinkShop linkShop = linkShopArrayList.get(i);
        TextView TVinLV = view.findViewById(R.id.idTVinLV);
        TVinLV.setText(linkShop.getName());

        return view;
    }
}
