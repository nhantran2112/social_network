package com.example.blog_javacode.store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.blog_javacode.MainActivity;
import com.example.blog_javacode.R;
import com.example.blog_javacode.RealPathUtil;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;
import com.example.blog_javacode.home.ImagePostRVAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.thanguit.toastperfect.ToastPerfect;

public class CreateProductActivity extends AppCompatActivity {

    private String account;
    private EditText ETnameproduct,ETbrandproduct,EToriginproduct,ETcategoryproduct,
            ETusedforproduct,ETdescripproduct,ETusesproduct;
    private Button idbtnaddimage,idbtnaddproduct;
    private TextView idclose,btnBack;
    private ImagePostRVAdapter imagePostRVAdapter;
    private String name_product ,brand ,origin , category , used_for , descrip, uses;
    int PICK_IMAGE_MULTIPLE = 1;
    private List<Uri> uriList;
    private List<MultipartBody.Part> partList;
    private ProgressBar idPB;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        nhandulieu();

        idbtnaddimage.setOnClickListener(view -> {
            uriList.clear();
            Intent intent = new Intent();

            // setting type to select to be image
            intent.setType("image/*");

            // allowing multiple image to be selected
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);

        });
        idclose.setOnClickListener(view -> {
            uriList.clear();
            idclose.setBackgroundColor(Color.WHITE);
            imagePostRVAdapter.notifyDataSetChanged();
        });
        btnBack.setOnClickListener(view ->{
            finish();
        });

        idbtnaddproduct.setOnClickListener(view -> {
            getText();
            for (int i = 0; i < uriList.size(); i++) {
                partList.add(createMultipart(uriList.get(i)));
            }
            if(name_product.length() >0 && brand.length()>0 && origin.length()>0
                    && category.length()>0 && used_for.length()>0 && descrip.length()>0
                    && uses.length()>0 && partList.size() > 0){
                idbtnaddproduct.setVisibility(View.GONE);
                idPB.setVisibility(View.VISIBLE);
                RequestBody requestBodyAccount = RequestBody.create(MediaType.parse("multipart/form-data"), account);
                RequestBody requestBodyname_product = RequestBody.create(MediaType.parse("multipart/form-data"), name_product);
                RequestBody requestBodybrand = RequestBody.create(MediaType.parse("multipart/form-data"), brand);
                RequestBody requestBodyorigin = RequestBody.create(MediaType.parse("multipart/form-data"), origin);
                RequestBody requestBodycategory = RequestBody.create(MediaType.parse("multipart/form-data"), category);
                RequestBody requestBodyused_for = RequestBody.create(MediaType.parse("multipart/form-data"), used_for);
                RequestBody requestBodydescrip = RequestBody.create(MediaType.parse("multipart/form-data"), descrip);
                RequestBody requestBodyuses= RequestBody.create(MediaType.parse("multipart/form-data"), uses);

                DataClient dataClient = APIUtils.getData();
                Call<String> callback = dataClient.createProduct(partList ,requestBodyAccount , requestBodyname_product,
                        requestBodybrand,requestBodyorigin,requestBodycategory,requestBodyused_for,requestBodydescrip,requestBodyuses);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        String result = response.body();
                        assert result != null;
                        if (result.equals("success")) {
                            MainActivity.mSocket.emit("client_send_mess", "createproduct");
                            ToastPerfect.makeText(CreateProductActivity.this, ToastPerfect.SUCCESS, "Đăng sản phẩm thành công", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d("AAAAA", t.getMessage());
                        ToastPerfect.makeText(CreateProductActivity.this, ToastPerfect.ERROR, "Đăng sản phẩm không thành công", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                        idbtnaddproduct.setVisibility(View.VISIBLE);
                        idPB.setVisibility(View.GONE);
                    }
                });

            }
            else
                ToastPerfect.makeText(CreateProductActivity.this, ToastPerfect.WARNING, "Vui lòng nhập đầy đủ thông tin", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();

        });

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    uriList.add(imageurl);
                }

            } else {
                Uri imageurl = data.getData();
                uriList.add(imageurl);
            }
            idclose.setBackgroundResource(R.drawable.custom_position_iv);
            imagePostRVAdapter.notifyDataSetChanged();
        } else {
            // show this if no image is selected
            ToastPerfect.makeText(CreateProductActivity.this, ToastPerfect.INFORMATION, "Bạn chưa chọn ảnh", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    private void nhandulieu(){
        account = getIntent().getStringExtra("account");

        ETnameproduct = findViewById(R.id.ETnameproduct);
        ETbrandproduct = findViewById(R.id.ETbrandproduct);
        EToriginproduct = findViewById(R.id.EToriginproduct);
        ETcategoryproduct = findViewById(R.id.ETcategoryproduct);
        ETusedforproduct = findViewById(R.id.ETusedforproduct);
        ETdescripproduct = findViewById(R.id.ETdescripproduct);
        ETusesproduct = findViewById(R.id.ETusesproduct);
        btnBack = findViewById(R.id.btnBack);

        idbtnaddimage = findViewById(R.id.idbtnaddimage);
        idbtnaddproduct = findViewById(R.id.idbtnaddproduct);

        idclose = findViewById(R.id.idclose);
        idPB = findViewById(R.id.idPB);

        uriList = new ArrayList<>();
        partList = new ArrayList<>();

        RecyclerView idRVImagePreview = findViewById(R.id.idRVImagePreview);
        imagePostRVAdapter = new ImagePostRVAdapter(null, R.layout.image_item_createproduct, uriList);
        idRVImagePreview.setAdapter(imagePostRVAdapter);
        imagePostRVAdapter.notifyDataSetChanged();

    }

    public MultipartBody.Part createMultipart(Uri uri) {
        String realpath = RealPathUtil.getRealPath(this, uri);
        File file = new File(realpath);
        String file_path = file.getAbsolutePath();
        String[] mangtenfile = file_path.split("\\.");
        file_path = mangtenfile[0] + System.currentTimeMillis() + "." + mangtenfile[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData("uploadFileproduct[]", file_path, requestBody);
    }
    private void getText(){
        name_product = ETnameproduct.getText().toString();
        brand = ETbrandproduct.getText().toString();
        origin = EToriginproduct.getText().toString();
        category = ETcategoryproduct.getText().toString();
        used_for = ETusedforproduct.getText().toString();
        descrip = ETdescripproduct.getText().toString();
        uses = ETusesproduct.getText().toString();
    }
}