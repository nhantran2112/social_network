package com.example.blog_javacode.people;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_javacode.MainActivity;
import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;
import com.example.blog_javacode.TimeAgo;
import com.example.blog_javacode.home.CmtPostActivity;
import com.example.blog_javacode.home.PostDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.thanguit.toastperfect.ToastPerfect;

public class PostRVAdapter extends RecyclerView.Adapter<PostRVAdapter.ViewHoldera> {
    private final ArrayList<Post> postArrayList;
    private final Context context;
    private final int layout;
    private final String account;


    public PostRVAdapter(ArrayList<Post> postArrayList, Context context, int layout, String account) {
        this.postArrayList = postArrayList;
        this.context = context;
        this.layout = layout;
        this.account = account;
    }

    @NonNull
    @Override
    public PostRVAdapter.ViewHoldera onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent,false);
        return new ViewHoldera(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PostRVAdapter.ViewHoldera holder, int position) {
        Post post = postArrayList.get(position);
        holder.idTVNamePersonPost.setText(post.getName());
        TimeAgo timeAgo = new TimeAgo();
        holder.idTVTimePost.setText(timeAgo.covertTimeToText(post.getTimes()));
        holder.idTVTextPost.setText(post.getText());
        Picasso.get().load(APIUtils.Base_Url_image+post.getAvatar()).into(holder.idIVAvaPersonPost);

        Picasso.get().load(APIUtils.Base_Url_image+post.getFirstImage()).into(holder.idIVImagePost);

        if(post.getCheckaccount()){
            holder.idbtndeletepost.setVisibility(View.VISIBLE);
            holder.idbtndeletepost.setOnClickListener(view -> {
                DataClient dataClient = APIUtils.getData();
                Call<String> callback = dataClient.deletePost(post.getId(),account);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        assert response.body() != null;
                        if(response.body().equals("success")){
                            MainActivity.mSocket.emit("client_send_mess", "createpost");
                            ToastPerfect.makeText(context, ToastPerfect.SUCCESS, "Xóa bài thành công", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d("deletepost",t.getLocalizedMessage());
                        ToastPerfect.makeText(context, ToastPerfect.ERROR, "Xóa không thành công", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                    }
                });
            });
        }
        else
            holder.idbtndeletepost.setVisibility(View.GONE);

        if(post.getLinks() != null){
            List<String> fimage;
            fimage = Arrays.asList(post.getLinks().split("\\|"));
            holder.idpositionIV.setText("1/"+ fimage.size());
        }
        else
            holder.idpositionIV.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context , PostDetailActivity.class);
            i.putExtra("idpost" , post.getId());
            i.putExtra("account" , account);
            i.putExtra("name" , post.getName());
            i.putExtra("avatar" , post.getAvatar());
            i.putExtra("text" , post.getText());
            i.putExtra("time" , post.getTimes());
            i.putExtra("link" , post.getLinks());

            context.startActivity(i);
        });

        holder.idTVReadCMT.setOnClickListener(view -> {
            Intent i = new Intent(context , CmtPostActivity.class);
            i.putExtra("idpost" , post.getId());
            i.putExtra("account" , account);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public static class ViewHoldera extends RecyclerView.ViewHolder{
        private final ImageView idIVAvaPersonPost;
        private final ImageView idIVImagePost;
        private final TextView idTVNamePersonPost;
        private final TextView idTVTimePost;
        private final TextView idTVTextPost;
        private final TextView idpositionIV;
        private final TextView idTVReadCMT;
        private final TextView idbtndeletepost;
        public ViewHoldera(@NonNull View itemView) {
            super(itemView);
            idIVAvaPersonPost = itemView.findViewById(R.id.idIVAvaPersonPost);
            idIVImagePost = itemView.findViewById(R.id.idIVImagePost);
            idTVNamePersonPost = itemView.findViewById(R.id.idTVNamePersonPost);
            idTVTimePost = itemView.findViewById(R.id.idTVTimePost);
            idTVTextPost = itemView.findViewById(R.id.idTVTextPost);
            idpositionIV = itemView.findViewById(R.id.idpositionIV);
            idTVReadCMT = itemView.findViewById(R.id.idTVReadCMT);
            idbtndeletepost = itemView.findViewById(R.id.idbtndeletepost);
        }
    }

}
