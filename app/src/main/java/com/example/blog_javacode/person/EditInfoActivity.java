package com.example.blog_javacode.person;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blog_javacode.MainActivity;
import com.example.blog_javacode.R;
import com.example.blog_javacode.RealPathUtil;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.thanguit.toastperfect.ToastPerfect;

public class EditInfoActivity extends AppCompatActivity {
    private String account;
    private String password;
    private String name;
    private String introduce;
    private String views;
    private String prodn;
    private String postn;
    private String avatar;

    private EditText ETaccount, ETpassword, ETname, ETintroduce;
    private ImageView idIVEditPerson;
    private TextView idTVSave,idTVChangeImage;

    private final int Request_Code_Image = 123;

    private MultipartBody.Part avapart;
    private Uri uri;
    private int checkchangeava = 0;
    private ProgressBar idPB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        nhandulieu();

        ETaccount.setOnClickListener(view -> ToastPerfect.makeText(EditInfoActivity.this, ToastPerfect.INFORMATION, "Bạn không thể chỉnh sửa tài khoản", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show());
        idIVEditPerson.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,Request_Code_Image);
        });
        idTVChangeImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,Request_Code_Image);
        });
        idTVSave.setOnClickListener(view -> {
            password = ETpassword.getText().toString();
            name = ETname.getText().toString();
            introduce = ETintroduce.getText().toString();
            if(password.length() >0 && name.length()>0){
                idTVSave.setVisibility(View.GONE);
                idPB.setVisibility(View.VISIBLE);
                DataClient dataClient = APIUtils.getData();
                Call<String> callback;
                if(checkchangeava == 0){
                    callback = dataClient.updateInfoNava(account,password,name,introduce);
                }
                else {
                    RequestBody requestBodyAccount = RequestBody.create(MediaType.parse("multipart/form-data"), account);
                    RequestBody requestBodypass = RequestBody.create(MediaType.parse("multipart/form-data"), password);
                    RequestBody requestBodyname = RequestBody.create(MediaType.parse("multipart/form-data"), name);
                    RequestBody requestBodyintro = RequestBody.create(MediaType.parse("multipart/form-data"), introduce);
                    avapart = convertmultipart(uri);

                    callback = dataClient.updateInfoWava(avapart , requestBodyAccount,requestBodypass,requestBodyname,requestBodyintro);
                }
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        String result = response.body();
                        assert result != null;
                        if (result.equals("success")) {
                            ToastPerfect.makeText(EditInfoActivity.this, ToastPerfect.SUCCESS, "Lưu thành công", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                            Intent i = new Intent(EditInfoActivity.this , MainActivity.class);
                            i.putExtra("account",account);
                            i.putExtra("password",password);
                            i.putExtra("name",name);
                            i.putExtra("introduce",introduce);
                            i.putExtra("views",views);
                            i.putExtra("prodn",prodn);
                            i.putExtra("postn",postn);
                            i.putExtra("avatar",avatar);
                            i.putExtra("token","1");
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d("AAAAA", t.getMessage());
                        ToastPerfect.makeText(EditInfoActivity.this, ToastPerfect.ERROR, "Lưu không thành công", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                        idTVSave.setVisibility(View.VISIBLE);
                        idPB.setVisibility(View.GONE);
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == Request_Code_Image && resultCode == RESULT_OK && data !=null){
            uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                idIVEditPerson.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            checkchangeava = 1;
        }
        else
            ToastPerfect.makeText(EditInfoActivity.this, ToastPerfect.INFORMATION, "Bạn chưa chọn ảnh", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void nhandulieu(){
        account = getIntent().getStringExtra("account");
        password = getIntent().getStringExtra("password");
        name = getIntent().getStringExtra("name");
        introduce = getIntent().getStringExtra("introduce");
        views = getIntent().getStringExtra("views");
        prodn = getIntent().getStringExtra("prodn");
        postn = getIntent().getStringExtra("postn");
        avatar = getIntent().getStringExtra("avatar");

        ETaccount = findViewById(R.id.ETaccount);
        ETpassword = findViewById(R.id.ETpassword);
        ETname = findViewById(R.id.ETname);
        ETintroduce = findViewById(R.id.ETintroduce);
        idIVEditPerson = findViewById(R.id.idIVEditPerson);
        idTVSave = findViewById(R.id.idTVSave);
        idTVChangeImage = findViewById(R.id.idTVChangeImage);
        idPB = findViewById(R.id.idPB);

        ETaccount.setText(account);
        ETpassword.setText(password);
        ETname.setText(name);
        ETintroduce.setText(introduce);
        Picasso.get().load(APIUtils.Base_Url_image + avatar).into(idIVEditPerson);

    }

    private MultipartBody.Part convertmultipart(Uri uri){
        String realpath = RealPathUtil.getRealPath(this, uri);
        File file = new File(realpath);
        String file_path = file.getAbsolutePath();
        String[] mangtenfile = file_path.split("\\.");
        file_path = mangtenfile[0] + System.currentTimeMillis() + "." + mangtenfile[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData("uploadavatar", file_path, requestBody);
    }
}