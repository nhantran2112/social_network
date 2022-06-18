package com.example.blog_javacode.home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.blog_javacode.MainActivity;
import com.example.blog_javacode.R;
import com.example.blog_javacode.Retrofit2.APIUtils;
import com.example.blog_javacode.Retrofit2.DataClient;
import com.example.blog_javacode.people.Post;
import com.example.blog_javacode.people.PostRVAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private final String account;
    private final String name;
    private final String avatar;

    private ArrayList<Post> postArrayList;
    private PostRVAdapter postRVAdapter;

    private SwipeRefreshLayout idSRL;

    public HomeFragment(String account, String name, String avatar) {
        this.account = account;
        this.name = name;
        this.avatar = avatar;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //anh xa
        ImageView idIVAvaPerson = view.findViewById(R.id.idIVAvaPerson);
        Picasso.get().load(APIUtils.Base_Url_image+ avatar).into(idIVAvaPerson);

        TextView idTVCreatePost = view.findViewById(R.id.idTVCreatePost);
        idTVCreatePost.setOnClickListener(view1 -> {
            Intent i = new Intent( HomeFragment.this.getActivity(), PopupCreatePostActivity.class);
            i.putExtra("account" , account);
            i.putExtra("name" , name);
            i.putExtra("avatar" , avatar);
            startActivity(i);
        });


        RecyclerView idRVPostPersonDetail = view.findViewById(R.id.idRVPostPersonDetail);
        postArrayList = new ArrayList<>();
        postRVAdapter = new PostRVAdapter(postArrayList,this.getActivity(),R.layout.post_rv_item,account);
        idRVPostPersonDetail.setAdapter(postRVAdapter);
        getPost();
        postRVAdapter.notifyDataSetChanged();

        idSRL = view.findViewById(R.id.idSRL);

        idSRL.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            getPost();
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
                getPost();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    


    public void getPost(){
        postArrayList.clear();
        DataClient dataClient = APIUtils.getData();
        Call<ArrayList<Post>> callback = dataClient.getPost("","");
        callback.enqueue(new Callback<ArrayList<Post>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ArrayList<Post>> call, @NonNull Response<ArrayList<Post>> response) {
                ArrayList<Post> modal = response.body();
                for(int i = 0; i< Objects.requireNonNull(modal).size(); i++){
                    boolean checkaccount = false;
                    if(account.equals(modal.get(i).getAccount())){
                        checkaccount = true;
                    }
                    Post post = new Post(modal.get(i).getAccount(),modal.get(i).getName(),modal.get(i).getAvatar(),
                            modal.get(i).getId(),modal.get(i).getText(),modal.get(i).getNoview(),modal.get(i).getNocmt(),
                            modal.get(i).getTimes(),modal.get(i).getLinks(),checkaccount);
                    postArrayList.add(post);
                }
                Log.d("GetPost","Lay post thanh cong");
                postRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Post>> call, @NonNull Throwable t) {
                Log.d("Error_getPost" , t.getLocalizedMessage());
            }
        });
    }

}