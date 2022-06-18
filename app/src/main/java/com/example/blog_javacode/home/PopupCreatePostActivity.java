package com.example.blog_javacode.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_javacode.MainActivity;
import com.example.blog_javacode.R;
import com.example.blog_javacode.RealPathUtil;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;
import com.squareup.picasso.Picasso;

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

public class PopupCreatePostActivity extends AppCompatActivity {
    private String account;
    private EditText idETTextPost;
    private Button idBtnCreatePost;
    private TextView idTVAddImagePost, idclose, btnBack;
    private ImagePostRVAdapter imagePostRVAdapter;
    private String text;
    int PICK_IMAGE_MULTIPLE = 1;
    private List<Uri> uriList;
    private List<MultipartBody.Part> partList;
    private ProgressBar idPB;



    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_create_post);


        nhandulieu();


        idTVAddImagePost.setOnClickListener(view -> {
            uriList.clear();
            // initialising intent
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
            idclose.setVisibility(View.GONE);
            imagePostRVAdapter.notifyDataSetChanged();
        });

        btnBack.setOnClickListener(view -> {
            finish();
        });

        idBtnCreatePost.setOnClickListener(view -> {
            text = idETTextPost.getText().toString();

            if (text.length() > 0) {
                idBtnCreatePost.setVisibility(View.GONE);
                idPB.setVisibility(View.VISIBLE);
                for (int i = 0; i < uriList.size(); i++) {
                    partList.add(createMultipart(uriList.get(i)));
                }
                RequestBody requestBodyAccount = RequestBody.create(MediaType.parse("multipart/form-data"), account);
                RequestBody requestBodyText = RequestBody.create(MediaType.parse("multipart/form-data"), text);

                DataClient dataClient = APIUtils.getData();
                Call<String> callback = dataClient.createPost(partList, requestBodyAccount, requestBodyText);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        String result = response.body();
                        assert result != null;
                        if (result.equals("success")) {
                            MainActivity.mSocket.emit("client_send_mess", "createpost");
                            ToastPerfect.makeText(PopupCreatePostActivity.this, ToastPerfect.SUCCESS, "Đăng bài thành công", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d("AAAAA", t.getMessage());
                        ToastPerfect.makeText(PopupCreatePostActivity.this, ToastPerfect.ERROR, "Đăng bài không thành công", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                        idBtnCreatePost.setVisibility(View.VISIBLE);
                        idPB.setVisibility(View.GONE);
                    }
                });
            } else
                ToastPerfect.makeText(PopupCreatePostActivity.this, ToastPerfect.WARNING, "Bạn không thể đăng bài nếu không có cap", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();

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
            idclose.setVisibility(View.VISIBLE);
            imagePostRVAdapter.notifyDataSetChanged();
        } else {
            // show this if no image is selected
            ToastPerfect.makeText(PopupCreatePostActivity.this, ToastPerfect.INFORMATION, "Bạn chưa chọn ảnh", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void nhandulieu() {
        account = getIntent().getStringExtra("account");
        String name = getIntent().getStringExtra("name");
        String avatar = getIntent().getStringExtra("avatar");

        TextView idTVNameCreatePost = findViewById(R.id.idTVNameCreatePost);
        ImageView idIVAvaCreatePost = findViewById(R.id.idIVAvaCreatePost);
        idETTextPost = findViewById(R.id.idETTextPost);
        idBtnCreatePost = findViewById(R.id.idBtnCreatePost);
        idTVAddImagePost = findViewById(R.id.idTVAddImagePost);
        btnBack = findViewById(R.id.btnBack);
        RecyclerView idRVImagePreview = findViewById(R.id.idRVImagePreview);
        idPB = findViewById(R.id.idPB);
        idclose = findViewById(R.id.idclose);
        uriList = new ArrayList<>();
        partList = new ArrayList<>();

        idTVNameCreatePost.setText(name);
        Picasso.get().load(APIUtils.Base_Url_image + avatar).into(idIVAvaCreatePost);


        imagePostRVAdapter = new ImagePostRVAdapter(null, R.layout.image_item_post, uriList);
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

        return MultipartBody.Part.createFormData("uploadFilepost[]", file_path, requestBody);
    }

}