package com.example.blog_javacode.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CmtPostRVAdapter extends RecyclerView.Adapter<CmtPostRVAdapter.ViewHolder> {
    private final ArrayList<Comment> commentArrayList;
    private final int layout;

    public CmtPostRVAdapter(ArrayList<Comment> commentArrayList, int layout) {
        this.commentArrayList = commentArrayList;
        this.layout = layout;
    }

    @NonNull
    @Override
    public CmtPostRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CmtPostRVAdapter.ViewHolder holder, int position) {
        Comment comment = commentArrayList.get(position);
        holder.idTVNamePersonCmtPost.setText(comment.getName());
        TimeAgo timeAgo = new TimeAgo();
        holder.idTVTimeCmtPost.setText(timeAgo.covertTimeToText(comment.getTimes()));
        holder.idTVTextCmtPost.setText(comment.getComment());
        Picasso.get().load(APIUtils.Base_Url_image+comment.getAvatar()).into(holder.idIVAvaPersonCmtPost);


    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView idIVAvaPersonCmtPost;
        private final TextView idTVNamePersonCmtPost;
        private final TextView idTVTimeCmtPost;
        private final TextView idTVTextCmtPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idIVAvaPersonCmtPost = itemView.findViewById(R.id.idIVAvaPersonCmtPost);
            idTVNamePersonCmtPost = itemView.findViewById(R.id.idTVNamePersonCmtPost);
            idTVTimeCmtPost = itemView.findViewById(R.id.idTVTimeCmtPost);
            idTVTextCmtPost = itemView.findViewById(R.id.idTVTextCmtPost);
        }
    }
}
