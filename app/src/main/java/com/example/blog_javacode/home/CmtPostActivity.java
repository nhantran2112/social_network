package com.example.blog_javacode.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;

import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_javacode.MainActivity;
import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.thanguit.toastperfect.ToastPerfect;

public class CmtPostActivity extends AppCompatActivity {
    private ArrayList<Comment> commentArrayList;
    private CmtPostRVAdapter cmtPostRVAdapter;

    private EditText idETAddCmtPost;

    private String comment;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmt_post);

        RecyclerView idRVCmtPost = findViewById(R.id.idRVCmtPost);
        commentArrayList = new ArrayList<>();
        cmtPostRVAdapter = new CmtPostRVAdapter(commentArrayList, R.layout.cmt_rv_item);
        idRVCmtPost.setAdapter(cmtPostRVAdapter);
        getDataCmt();
        cmtPostRVAdapter.notifyDataSetChanged();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width, (int) (height * .98));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.x = 0;
        params.y = 0;

        getWindow().setAttributes(params);

        idETAddCmtPost = findViewById(R.id.idETAddCmtPost);
        TextView idTVInsertCmt = findViewById(R.id.idTVInsertCmt);

        idTVInsertCmt.setOnClickListener(view -> {
            comment = idETAddCmtPost.getText().toString();

            if (comment.length() > 0) {
                DataClient dataClient = APIUtils.getData();
                Call<String> callback = dataClient.insertCmtPost(getIntent().getStringExtra("idpost") + "-post", getIntent().getStringExtra("account"), comment);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        String result = response.body();
                        assert result != null;
                        if (result.equals("success")) {
                            MainActivity.mSocket.emit("client_send_mess", "createcmtpost");
                            ToastPerfect.makeText(CmtPostActivity.this , ToastPerfect.SUCCESS , "Bình luận thành công" ,ToastPerfect.CENTER , ToastPerfect.LENGTH_SHORT).show();
                            idETAddCmtPost.setText("");
                        } else
                            ToastPerfect.makeText(CmtPostActivity.this , ToastPerfect.ERROR , "Bình luận không thành công vui lòng thử lại" ,ToastPerfect.CENTER , ToastPerfect.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d("Error_createCmtPost", t.getLocalizedMessage());
                    }
                });
            }
        });

        MainActivity.mSocket.on("serverguitinnhan" , onRetrieveData);

    }
    private final Emitter.Listener onRetrieveData = args -> runOnUiThread(() -> {
        JSONObject object = (JSONObject) args[0];
        try {
            String ten = object.getString("noidung");
            if(ten.equals("createcmtpost")){
                getDataCmt();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    private void getDataCmt() {
        commentArrayList.clear();
        DataClient dataClient = APIUtils.getData();
        Call<ArrayList<Comment>> callback = dataClient.getCmtPost(getIntent().getStringExtra("idpost") + "-post");
        callback.enqueue(new Callback<ArrayList<Comment>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ArrayList<Comment>> call, @NonNull Response<ArrayList<Comment>> response) {
                ArrayList<Comment> modal = response.body();
                for (int i = 0; i < Objects.requireNonNull(modal).size(); i++) {
                    Comment comment = new Comment(modal.get(i).getId(), modal.get(i).getAccount(), modal.get(i).getName(),
                            modal.get(i).getAvatar(), modal.get(i).getComment(), modal.get(i).getTimes());
                    commentArrayList.add(comment);
                }
                cmtPostRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Comment>> call, @NonNull Throwable t) {
                Log.d("Error_getDatacmt", t.getLocalizedMessage());
            }
        });
    }
}