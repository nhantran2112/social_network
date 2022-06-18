package com.example.blog_javacode.home;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagePostRVAdapter extends RecyclerView.Adapter<ImagePostRVAdapter.ImageViewHolder> {
    private final List<String> imageList;
    private final List<Uri> uriList;
    private final int layout;

    public ImagePostRVAdapter(List<String> imageList, int layout, List<Uri> uriList) {
        this.imageList = imageList;
        this.layout = layout;
        this.uriList = uriList;
    }

    @NonNull
    @Override
    public ImagePostRVAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ImagePostRVAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePostRVAdapter.ImageViewHolder holder, int position) {
        if (imageList != null) {
            Picasso.get().load(APIUtils.Base_Url_image + imageList.get(position)).into(holder.idIVImagePostDetail);
        } else {
            holder.idIVImagePostDetail.setImageURI(uriList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (imageList != null) {
            return imageList.size();
        } else {
            return uriList.size();
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView idIVImagePostDetail;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            idIVImagePostDetail = itemView.findViewById(R.id.idIVImagePostDetail);
        }
    }
}
