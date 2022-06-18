package com.example.blog_javacode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;

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

public class RegisterActivity extends AppCompatActivity {
    private ImageView idIVEditReg ;
    private TextView idTVChangeImageReg, idTVlogin;
    private EditText ETaccountReg, ETpasswordReg, ETnameReg;
    private Button idbtnregister;
    private String account , password , name  ;
    private int checkchangeava = 0;
    private final int Request_Code_Image = 123;
    private MultipartBody.Part avapart;
    private Uri uri;
    private ProgressBar idPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        anhxa();

        idTVlogin.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this , LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
        idIVEditReg.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,Request_Code_Image);
        });

        idTVChangeImageReg.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,Request_Code_Image);
        });

        idbtnregister.setOnClickListener(view -> {
            account = ETaccountReg.getText().toString();
            password = ETpasswordReg.getText().toString();
            name = ETnameReg.getText().toString();

            if(account.length()>0 && password.length() >0 && name.length()>0){
                idbtnregister.setVisibility(View.GONE);
                idPB.setVisibility(View.VISIBLE);
                DataClient dataClient = APIUtils.getData();
                Call<String> callback;
                if(checkchangeava == 0){
                    callback = dataClient.registerNava(account,password,name);
                }
                else {
                    RequestBody requestBodyAccount = RequestBody.create(MediaType.parse("multipart/form-data"), account);
                    RequestBody requestBodypass = RequestBody.create(MediaType.parse("multipart/form-data"), password);
                    RequestBody requestBodyname = RequestBody.create(MediaType.parse("multipart/form-data"), name);
                    avapart = convertmultipart(uri);

                    callback = dataClient.registerWava(avapart , requestBodyAccount,requestBodypass,requestBodyname);
                }
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        assert response.body() != null;
                        if(response.body().equals("success")){
                            ToastPerfect.makeText(RegisterActivity.this, ToastPerfect.SUCCESS, "Đăng ký thành công \n Vui lòng đăng nhập lại ", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                            idbtnregister.setVisibility(View.VISIBLE);
                            idPB.setVisibility(View.GONE);
                            finish();
                        }
                        else{
                            ToastPerfect.makeText(RegisterActivity.this, ToastPerfect.WARNING, "Tài khoản đã có người sử dụng", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                            ETaccountReg.setText("");
                            ETpasswordReg.setText("");
                            idbtnregister.setVisibility(View.VISIBLE);
                            idPB.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d("register" , t.getLocalizedMessage());
                        ToastPerfect.makeText(RegisterActivity.this, ToastPerfect.ERROR, "Đăng ký không thành công \n Vui lòng kiểm tra lại", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                        idbtnregister.setVisibility(View.VISIBLE);
                        idPB.setVisibility(View.GONE);
                    }
                });
            }
            else
                ToastPerfect.makeText(RegisterActivity.this, ToastPerfect.WARNING, "Vui lòng nhập đủ thông tin ", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == Request_Code_Image && resultCode == RESULT_OK && data !=null){
            uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                idIVEditReg.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            checkchangeava = 1;
        }
        else
            ToastPerfect.makeText(RegisterActivity.this, ToastPerfect.INFORMATION, "Bạn chưa chọn ảnh", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void anhxa(){
        idIVEditReg = findViewById(R.id.idIVEditReg);
        idTVChangeImageReg = findViewById(R.id.idTVChangeImageReg);
        idTVlogin = findViewById(R.id.idTVlogin);
        ETaccountReg = findViewById(R.id.ETaccountReg);
        ETpasswordReg = findViewById(R.id.ETpasswordReg);
        ETnameReg = findViewById(R.id.ETnameReg);
        idbtnregister = findViewById(R.id.idbtnregister);
        idPB = findViewById(R.id.idPB);
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