package com.example.blog_javacode.store;

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


public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.ViewHolder> {
    private final ArrayList<Product> productArrayList;
    private final Context context;
    private final int layout;

    public ProductRVAdapter(ArrayList<Product> productArrayList, Context context, int layout) {
        this.productArrayList = productArrayList;
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ProductRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRVAdapter.ViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.idTVNameProduct.setText(product.getName());
        holder.idTVCategoryProduct.setText(product.getCategory());
        holder.idTVUseforProduct.setText(product.getUsedFor());
        Picasso.get().load(APIUtils.Base_Url_image + product.getFirstImage()).into(holder.idIVProduct);
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context, ProductDeatilActivity.class);
            i.putExtra("id", product.getId());
            i.putExtra("name", product.getName());
            i.putExtra("brand", product.getBrand());
            i.putExtra("origin", product.getOrigin());
            i.putExtra("category", product.getCategory());
            i.putExtra("usedFor", product.getUsedFor());
            i.putExtra("descrip", product.getDescrip());
            i.putExtra("uses", product.getUses());
            i.putExtra("images", product.getImages());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView idIVProduct;
        private final TextView idTVNameProduct;
        private final TextView idTVCategoryProduct;
        private final TextView idTVUseforProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idIVProduct = itemView.findViewById(R.id.idIVProduct);
            idTVNameProduct = itemView.findViewById(R.id.idTVNameProduct);
            idTVCategoryProduct = itemView.findViewById(R.id.idTVCategoryProduct);
            idTVUseforProduct = itemView.findViewById(R.id.idTVUseforProduct);
        }
    }
}
