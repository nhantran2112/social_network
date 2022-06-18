package com.example.blog_javacode.people;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PeopleRVAdapter extends RecyclerView.Adapter<PeopleRVAdapter.ViewHolder> {
    private final ArrayList<Person> personArrayList;
    private final Context context;
    private final int layout;
    private final String account;

    public PeopleRVAdapter(ArrayList<Person> personArrayList, Context context, int layout, String account) {
        this.personArrayList = personArrayList;
        this.context = context;
        this.layout = layout;
        this.account = account;
    }

    @NonNull
    @Override
    public PeopleRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PeopleRVAdapter.ViewHolder holder, int position) {
        Person person = personArrayList.get(position);
        holder.idTVNameOtherPeople.setText(person.getName());
        holder.idTVViewOtherPeople.setText(person.getView().toString());
        holder.idTVProdNOtherPeople.setText(person.getProductNumber().toString());
        holder.idTVPostNOtherPeople.setText(person.getPostNumber().toString());
        Picasso.get().load(APIUtils.Base_Url_image + person.getAvatar()).into(holder.idIVOtherPeople);
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context,PersonDetailActivity.class);
            i.putExtra("account_main" , account);
            i.putExtra("account" , person.getAccount());
            i.putExtra("name" , person.getName());
            i.putExtra("view" , person.getView().toString());
            i.putExtra("prodn" , person.getProductNumber().toString());
            i.putExtra("postn" , person.getPostNumber().toString());
            i.putExtra("introduce" , person.getIntro());
            i.putExtra("avatar" , person.getAvatar());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return personArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView idIVOtherPeople;
        private final TextView idTVNameOtherPeople;
        private final TextView idTVViewOtherPeople;
        private final TextView idTVProdNOtherPeople;
        private final TextView idTVPostNOtherPeople;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idIVOtherPeople = itemView.findViewById(R.id.idIVOtherPeople);
            idTVNameOtherPeople = itemView.findViewById(R.id.idTVNameOtherPeople);
            idTVViewOtherPeople = itemView.findViewById(R.id.idTVViewOtherPeople);
            idTVProdNOtherPeople = itemView.findViewById(R.id.idTVProdNOtherPeople);
            idTVPostNOtherPeople = itemView.findViewById(R.id.idTVPostNOtherPeople);
        }
    }
}
