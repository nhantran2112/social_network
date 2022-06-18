package com.example.blog_javacode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;
import com.example.blog_javacode.people.Person;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.thanguit.toastperfect.ToastPerfect;

public class LoginActivity extends AppCompatActivity {
    private String account , password;
    private EditText ETaccount,ETpassword;
    private Button idbtnlogin;
    private TextView idTVRegister;
    private ProgressBar idPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        anhxa();

        idbtnlogin.setOnClickListener(view -> {
            account = ETaccount.getText().toString();
            password = ETpassword.getText().toString();
            if (account.length()>0 && password.length()>0){
                idbtnlogin.setVisibility(View.GONE);
                idPB.setVisibility(View.VISIBLE);
                DataClient dataClient = APIUtils.getData();
                Call<Person> callback = dataClient.login(account,password);
                callback.enqueue(new Callback<Person>() {
                    @Override
                    public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {
                        if(response.body() != null){
                            Person modal = response.body();
                            Intent i = new Intent(LoginActivity.this , MainActivity.class);
                            i.putExtra("account" , account);
                            i.putExtra("password" , password);
                            i.putExtra("name" , modal.getName());
                            i.putExtra("introduce" , modal.getIntro());
                            i.putExtra("views" , modal.getView().toString());
                            i.putExtra("prodn" , modal.getProductNumber().toString());
                            i.putExtra("postn" , modal.getPostNumber().toString());
                            i.putExtra("avatar" , modal.getAvatar());
                            idbtnlogin.setVisibility(View.VISIBLE);
                            idPB.setVisibility(View.GONE);
                            startActivity(i);
                        }
                        else {
                            ToastPerfect.makeText(LoginActivity.this, ToastPerfect.ERROR, "Tài khoản hoặc mật khẩu không chính xác", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                            idbtnlogin.setVisibility(View.VISIBLE);
                            idPB.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
                        Log.d("login" ,t.getLocalizedMessage());
                        ToastPerfect.makeText(LoginActivity.this, ToastPerfect.ERROR, "Đăng nhập không thành công \n Vui lòng kiểm tra lại", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
                        idbtnlogin.setVisibility(View.VISIBLE);
                        idPB.setVisibility(View.GONE);
                    }
                });
            }
            else
                ToastPerfect.makeText(LoginActivity.this, ToastPerfect.WARNING, "Vui lòng nhập đầy đủ thông tin", ToastPerfect.CENTER, ToastPerfect.LENGTH_SHORT).show();
        });

        idTVRegister.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this , RegisterActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
    }

    private void anhxa(){
        ETaccount = findViewById(R.id.ETaccount);
        ETpassword = findViewById(R.id.ETpassword);
        idbtnlogin = findViewById(R.id.idbtnlogin);
        idTVRegister = findViewById(R.id.idTVRegister);
        idPB = findViewById(R.id.idPB);
    }
}