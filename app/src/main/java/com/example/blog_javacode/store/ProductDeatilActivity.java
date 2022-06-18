package com.example.blog_javacode.store;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_javacode.MyGesture;
import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDeatilActivity extends AppCompatActivity {
    private String id;

    private ArrayList<LinkShop> linkShopArrayList;
    private LinkShopLVAdapter linkShopLVAdapter;
    private GestureDetector gestureDetector;


    private ArrayList<Product> OtherproductArrayList;
    private ProductRVAdapter otherproductRVAdapter;


    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_deatil);
        //lay du lieu tu store fragment
        id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String brand = getIntent().getStringExtra("brand");
        String origin = getIntent().getStringExtra("origin");
        String category = getIntent().getStringExtra("category");
        String usedfor = getIntent().getStringExtra("usedfor");
        String descrip = getIntent().getStringExtra("descrip");
        String uses = getIntent().getStringExtra("uses");
        String images = getIntent().getStringExtra("images");

        //anh xa
        ImageView idIVProductDetail = findViewById(R.id.idIVProductDetail);
        TextView idTVNameProductDetail = findViewById(R.id.idTVNameProductDetail);
        TextView idTVOrigin = findViewById(R.id.idTVOrigin);
        TextView idTVBrand = findViewById(R.id.idTVBrand);
        TextView idTVDescrip = findViewById(R.id.idTVDescrip);
        TextView idTVUseDetail = findViewById(R.id.idTVUseDetail);
        TextView idpositionIV = findViewById(R.id.idpositionIV);
        TextView btnBack = findViewById(R.id.btnBack);

        //set text
        idTVNameProductDetail.setText(name);
        idTVOrigin.setText("Xuất xứ: "+ origin);
        idTVBrand.setText("Thương hiệu: "+ brand);
        idTVDescrip.setText(descrip);
        idTVUseDetail.setText(uses);

        btnBack.setOnClickListener(view ->{
            finish();
        });

        //lay hinh anh dau tien
        List<String> fimage = Arrays.asList(images.split("\\|"));
        Picasso.get().load(APIUtils.Base_Url_image+ fimage.get(0)).into(idIVProductDetail);

        //tao listview linkshop
        ListView listViewlinkshop = findViewById(R.id.idLVLinkshop);
        linkShopArrayList = new ArrayList<>();
        linkShopLVAdapter = new LinkShopLVAdapter(linkShopArrayList , this,R.layout.textview);
        listViewlinkshop.setOnItemClickListener((adapterView, view, i, l) -> {
            LinkShop linkShop = (LinkShop) linkShopLVAdapter.getItem(i);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(linkShop.getLinkShop()));
            startActivity(intent);
        });
        listViewlinkshop.setAdapter(linkShopLVAdapter);
        getLinkShop();
        linkShopLVAdapter.notifyDataSetChanged();

        //tao recyclerview other product
        RecyclerView idRVOtherProduct = findViewById(R.id.idRVOtherProduct);
        OtherproductArrayList = new ArrayList<>();
        otherproductRVAdapter = new ProductRVAdapter(OtherproductArrayList,this,R.layout.other_product);
        idRVOtherProduct.setAdapter(otherproductRVAdapter);
        getProduct(category);
        otherproductRVAdapter.notifyDataSetChanged();

        idpositionIV.setText("1/"+ fimage.size());
        gestureDetector = new GestureDetector(this,new MyGesture(fimage,idIVProductDetail,idpositionIV ));

        idIVProductDetail.setOnTouchListener((view, motionEvent) -> {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        });
    }

    private void getLinkShop(){
        DataClient dataClient = APIUtils.getData();
        Call<ArrayList<LinkShop>> callback = dataClient.getLinkShop(id);
        callback.enqueue(new Callback<ArrayList<LinkShop>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<LinkShop>> call, @NonNull Response<ArrayList<LinkShop>> response) {
                ArrayList<LinkShop> modal = response.body();
                for(int i = 0; i< Objects.requireNonNull(modal).size(); i++){
                    LinkShop linkShop = new LinkShop(modal.get(i).getName(), modal.get(i).getLinkShop());
                    linkShopArrayList.add(linkShop);
                }
                Log.d("AAAA" , "lAY DU LIEU THANH CONG");
                linkShopLVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<LinkShop>> call, @NonNull Throwable t) {
                Log.d("AAAA" , t.getLocalizedMessage());
            }
        });
    }

    private  void getProduct(String category){
        DataClient dataClient = APIUtils.getData();
        Call<ArrayList<Product>> callback = dataClient.getOtherProduct(category);
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
                    OtherproductArrayList.add(product);
                }
                otherproductRVAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                Log.d("AAAA", t.getLocalizedMessage());
            }
        });
    }
}