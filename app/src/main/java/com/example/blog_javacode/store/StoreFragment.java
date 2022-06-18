package com.example.blog_javacode.store;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.blog_javacode.MainActivity;
import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;
import com.example.blog_javacode.article.CategoryRVAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoreFragment extends Fragment implements CategoryRVAdapter.CategoryClickInterface {

    private ArrayList<Product> productArrayList;
    private ProductRVAdapter productRVAdapter;

    private ArrayList<String> categoryArrayList;
    private CategoryRVAdapter categoryRVAdapter;

    private SwipeRefreshLayout idSRL;

    private final String datasearch;

    public StoreFragment(String datasearch) {
        this.datasearch = datasearch;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        RecyclerView idRVProduct = view.findViewById(R.id.idRVProduct);
        productArrayList = new ArrayList<>();
        productRVAdapter = new ProductRVAdapter(productArrayList, this.getActivity(),R.layout.product_rv_item);
        idRVProduct.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        idRVProduct.setAdapter(productRVAdapter);
        getProduct("Tất cả");
        productRVAdapter.notifyDataSetChanged();

        RecyclerView idRVCategoriesArticle = view.findViewById(R.id.idRVCategoriesArticle);
        categoryArrayList = new ArrayList<>();
        categoryRVAdapter = new CategoryRVAdapter(categoryArrayList, this.getActivity(), this);
        idRVCategoriesArticle.setAdapter(categoryRVAdapter);
        categoryRVAdapter.notifyDataSetChanged();

        idSRL = view.findViewById(R.id.idSRLSF);

        idSRL.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            getProduct("Tất cả");
            idSRL.setRefreshing(false);
        }, 2500));
        MainActivity.mSocket.on("serverguitinnhan" , onRetrieveData);


        return view;
    }

    private final Emitter.Listener onRetrieveData = args -> requireActivity().runOnUiThread(() -> {
        JSONObject object = (JSONObject) args[0];
        try {
            String ten = object.getString("noidung");
            if(ten.equals("createpost")){
                getProduct("Tất cả");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    private  void getProduct(String category){
        productArrayList.clear();
        DataClient dataClient = APIUtils.getData();
        Call<ArrayList<Product>> callback = dataClient.getProducts("",datasearch);
        callback.enqueue(new Callback<ArrayList<Product>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                ArrayList<Product> modal = response.body();
                if(category.equals("Tất cả")){
                    for (int i = 0; i < Objects.requireNonNull(modal).size(); i++){
                        Product product = new Product(modal.get(i).getId(), modal.get(i).getAccount(), modal.get(i).getName(),
                                modal.get(i).getBrand(), modal.get(i).getOrigin(), modal.get(i).getCategory(),
                                modal.get(i).getUsedFor(), modal.get(i).getDescrip(),modal.get(i).getUses(),
                                modal.get(i).getTimes(), modal.get(i).getNOCmt(),modal.get(i).getImages());
                        productArrayList.add(product);
                    }
                }
                else {
                    for (int i = 0; i < Objects.requireNonNull(modal).size(); i++){
                        if(modal.get(i).getCategory().equals(category)) {
                            Product product = new Product(modal.get(i).getId(), modal.get(i).getAccount(), modal.get(i).getName(),
                                    modal.get(i).getBrand(), modal.get(i).getOrigin(), modal.get(i).getCategory(),
                                    modal.get(i).getUsedFor(), modal.get(i).getDescrip(),modal.get(i).getUses(),
                                    modal.get(i).getTimes(), modal.get(i).getNOCmt(),modal.get(i).getImages());
                            productArrayList.add(product);
                        }
                    }
                }
                if(categoryArrayList.isEmpty()){
                    categoryArrayList.add("Tất cả");
                    for(int i =1 ; i<productArrayList.size() ; i++){
                        if(categoryArrayList.size()==1){
                            categoryArrayList.add(modal.get(0).getCategory());
                        }
                        else{
                            for(int j =0 ; j<categoryArrayList.size(); j++){
                                if(modal.get(i).getCategory().equals(categoryArrayList.get(j))){
                                    break;
                                }
                                if(j == categoryArrayList.size()-1)
                                    categoryArrayList.add(modal.get(i).getCategory());
                            }
                        }
                    }
                }

                categoryRVAdapter.notifyDataSetChanged();
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                Log.d("BBBB" , t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryArrayList.get(position);
        getProduct(category);
    }
}