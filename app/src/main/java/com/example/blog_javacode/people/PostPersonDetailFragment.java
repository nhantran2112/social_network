package com.example.blog_javacode.people;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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


public class PostPersonDetailFragment extends Fragment {
    private ArrayList<Post> postArrayList;
    private PostRVAdapter postRVAdapter;
    private final String account;
    private final String account_main;

    private final String datasearch;

    public PostPersonDetailFragment(String account,String account_main, String datasearch) {
        this.account = account;
        this.account_main = account_main;
        this.datasearch = datasearch;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_person_detail, container, false);

        RecyclerView idRVPostPersonDetail = view.findViewById(R.id.idRVPostPersonDetail);
        postArrayList = new ArrayList<>();
        postRVAdapter = new PostRVAdapter(postArrayList,this.getActivity(),R.layout.post_rv_item,account_main);
        idRVPostPersonDetail.setAdapter(postRVAdapter);
        getPost();
        postRVAdapter.notifyDataSetChanged();

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
        Call<ArrayList<Post>> callback = dataClient.getPost(account ,datasearch);
        callback.enqueue(new Callback<ArrayList<Post>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ArrayList<Post>> call, @NonNull Response<ArrayList<Post>> response) {
                ArrayList<Post> modal = response.body();
                for(int i = 0; i< Objects.requireNonNull(modal).size(); i++){
                    boolean checkaccount = false;
                    if(account_main.equals(modal.get(i).getAccount())){
                        checkaccount = true;
                    }
                    Post post = new Post(modal.get(i).getAccount(),modal.get(i).getName(),modal.get(i).getAvatar(),
                            modal.get(i).getId(),modal.get(i).getText(),modal.get(i).getNoview(),modal.get(i).getNocmt(),
                            modal.get(i).getTimes(),modal.get(i).getLinks() , checkaccount);
                    postArrayList.add(post);
                }
                Log.d("AAAA","Lay post thanh cong");
                postRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Post>> call, @NonNull Throwable t) {
                Log.d("AAAA" , t.getLocalizedMessage());
            }
        });
    }

}