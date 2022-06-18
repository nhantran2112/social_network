package com.example.blog_javacode.people;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blog_javacode.MainActivity;
import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;
import com.example.blog_javacode.store.Product;
import com.example.blog_javacode.store.ProductRVAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPersonDetailFragment extends Fragment {
    private ArrayList<Product> productArrayList;
    private ProductRVAdapter productRVAdapter;
    private final String account;

    public ProductPersonDetailFragment(String account) {
        this.account = account;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_person_detail, container, false);

        RecyclerView idRVProductPersonDetail = view.findViewById(R.id.idRVProductPersonDetail);
        productArrayList = new ArrayList<>();
        productRVAdapter = new ProductRVAdapter(productArrayList, this.getActivity(),R.layout.product_rv_item);
        idRVProductPersonDetail.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        idRVProductPersonDetail.setAdapter(productRVAdapter);
        getProduct();
        productRVAdapter.notifyDataSetChanged();

        MainActivity.mSocket.on("serverguitinnhan" , onRetrieveData);


        return view;
    }

    private final Emitter.Listener onRetrieveData = args -> requireActivity().runOnUiThread(() -> {
        JSONObject object = (JSONObject) args[0];
        try {
            String ten = object.getString("noidung");
            if(ten.equals("createproduct")){
                getProduct();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    private  void getProduct(){
        productArrayList.clear();
        DataClient dataClient = APIUtils.getData();
        Call<ArrayList<Product>> callback = dataClient.getProducts(account,"");
        callback.enqueue(new Callback<ArrayList<Product>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                ArrayList<Product> modal = response.body();
                for (int i = 0; i < Objects.requireNonNull(modal).size(); i++){
                    Product product = new Product(modal.get(i).getId(), modal.get(i).getAccount(), modal.get(i).getName(),
                            modal.get(i).getBrand(), modal.get(i).getOrigin(), modal.get(i).getCategory(),
                            modal.get(i).getUsedFor(), modal.get(i).getDescrip(),modal.get(i).getUses(),
                            modal.get(i).getTimes(), modal.get(i).getNOCmt(),modal.get(i).getImages());
                    productArrayList.add(product);
                }
                Log.d("AAAA","Lay product thanh cong");
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                Log.d("BBBB" , t.getLocalizedMessage());
            }
        });
    }
}